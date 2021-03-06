package io.mobile.citylocationviewer.data.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import io.mobile.citylocationviewer.data.CitiesFileStreamProvider;
import io.mobile.citylocationviewer.data.CitiesRepository;
import io.mobile.citylocationviewer.model.City;

public class CitiesRepositoryImpl implements CitiesRepository {


    private static CitiesRepositoryImpl INSTANCE;

    private final String TAG = this.getClass().getSimpleName();
    private CitiesFileStreamProvider fileStreamProvider;

    private volatile boolean isInitiated = false;
    private final List<City> cities = new ArrayList<>(200000);


    private CitiesRepositoryImpl(CitiesFileStreamProvider fileStreamProvider) {
        this.fileStreamProvider = fileStreamProvider;
    }

    public static synchronized CitiesRepository getInstance(CitiesFileStreamProvider fileStreamProvider) {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRepositoryImpl(fileStreamProvider);
        }
        return INSTANCE;
    }

    @Override
    public void init() {
        if (isInitiated()) return;

        isInitiated = true;

        try {
            InputStreamReader streamReader = new InputStreamReader(fileStreamProvider.getInputStream(), "UTF-8");
            JsonReader reader = new JsonReader(streamReader);

            try {
                fillCitiesList(reader);
                sortByCityName(cities);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                streamReader.close();
                reader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<City> searchByPrefix(String prefix) {
        if (!isInitiated()) init();

        if (prefix == null || prefix.trim().isEmpty()) return cities;

        String searchPrefix = prefix.toLowerCase();

        return searchByPrefixInternal(searchPrefix);
    }

    private boolean isInitiated() {
        return isInitiated;
    }

    private void fillCitiesList(JsonReader reader) throws Exception {
        Gson gson = new GsonBuilder().create();
        reader.beginArray();

        while (reader.hasNext()) {
            City city = gson.fromJson(reader, City.class);
            cities.add(city);
        }
    }

    private void sortByCityName(List<City> data) {
        Collections.sort(data, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                int result = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
                return (result != 0) ? result : o1.getName().compareTo(o2.getName());
            }
        });
    }

    private List<City> searchByPrefixInternal(String prefix) {
        List<City> filteredList = new LinkedList<>();
        for (City city : cities) {
            String cityName = city.getName().toLowerCase();
            if (cityName.startsWith(prefix)) filteredList.add(city);
        }

        return filteredList;
    }
}
