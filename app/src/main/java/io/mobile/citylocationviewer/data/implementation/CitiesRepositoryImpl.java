package io.mobile.citylocationviewer.data.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import io.mobile.citylocationviewer.data.CitiesFileProvider;
import io.mobile.citylocationviewer.data.CitiesRepository;
import io.mobile.citylocationviewer.model.City;

public class CitiesRepositoryImpl implements CitiesRepository {


    private final String TAG = this.getClass().getSimpleName();
    private CitiesFileProvider fileProvider;

    private final List<City> cities = new ArrayList<>(10000);


    public CitiesRepositoryImpl(CitiesFileProvider fileProvider) {
        this.fileProvider = fileProvider;
    }


    @Override
    public void init() {
        if (isInitiated()) return;

        try {
            InputStream fileInputStream = new FileInputStream(fileProvider.getFile());
            InputStreamReader streamReader = new InputStreamReader(fileInputStream, "UTF-8");
            JsonReader reader = new JsonReader(streamReader);

            try {
                fillCitiesList(reader);
                sortByCityName(cities);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fileInputStream.close();
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

        List<City> filteredList = new LinkedList<>();
        for (City city : cities) {
            String cityName = city.getName().toLowerCase();
            String searchPrefix = prefix.toLowerCase();
            if (cityName.startsWith(searchPrefix)) filteredList.add(city);
        }

        return filteredList;
    }

    private boolean isInitiated() {
        return !cities.isEmpty();
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
}
