package io.mobile.citylocationviewer;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import io.mobile.citylocationviewer.data.CitiesFileStreamProvider;
import io.mobile.citylocationviewer.data.CitiesRepository;
import io.mobile.citylocationviewer.data.implementation.CitiesRepositoryImpl;
import io.mobile.citylocationviewer.model.City;

public class CitiesRepositoryTest {


    private CitiesFileStreamProvider fileProvider = new CitiesFileStreamProvider() {

        @Override
        public InputStream getInputStream() {
            try {
                URL urlToFile = CitiesRepositoryTest.this.getClass().getClassLoader().getResource("cities.json");
                return new FileInputStream(new File(urlToFile.getFile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    };


    @Test
    public void initTimeTest() {
        long startTime = System.currentTimeMillis();

        getInitiatedRepository();

        System.out.println("Init time (ms): " + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void searchByPrefixTest() {
        String prefix = "A";
        Assert.assertTrue(allCitiesStartedWithPrefix(prefix));
    }

    @Test
    public void searchByLowerPrefixTest() {
        String prefix = "j";
        Assert.assertTrue(allCitiesStartedWithPrefix(prefix));
    }

    @Test
    public void searchByLowerBigPrefixTest() {
        String prefix = "ABC";
        Assert.assertTrue(allCitiesStartedWithPrefix(prefix));
    }

    @Test
    public void searchByBlankPrefixTest() {
        String prefix = "       ";
        List<City> result = searchByPrefix(prefix);
        Assert.assertTrue(!result.isEmpty());
    }

    @Test
    public void searchByInvalidPrefixTest() {
        String prefix = "-=+";
        List<City> result = searchByPrefix(prefix);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void searchByNullPrefixTest() {
        String prefix = null;
        List<City> result = searchByPrefix(prefix);
        Assert.assertTrue(!result.isEmpty());
    }

    @Test
    public void checkAlphabeticOrderInSearchTest() {
        String prefix = "M";
        List<City> result = searchByPrefix(prefix);

        boolean orderIsCorrect = true;
        for (int i = 0; i < result.size() - 1; i++) {
            City currentCity = result.get(i);
            City nextCity = result.get(i + 1);

            if (String.CASE_INSENSITIVE_ORDER.compare(currentCity.getName(), nextCity.getName()) > 0) {
                orderIsCorrect = false;
            }
        }

        Assert.assertTrue(orderIsCorrect);
    }

    private CitiesRepository getInitiatedRepository() {
        CitiesRepository repository = new CitiesRepositoryImpl(fileProvider);
        repository.init();
        return repository;
    }

    private List<City> searchByPrefix(String prefix) {
        return getInitiatedRepository().searchByPrefix(prefix);
    }

    private boolean allCitiesStartedWithPrefix(String prefix) {
        List<City> cities = searchByPrefix(prefix);
        String searchPrefix = prefix.toLowerCase();
        for (City city : cities) {
            String cityName = city.getName().toLowerCase();
            if (!cityName.startsWith(searchPrefix)) return false;
        }
        return true;
    }
}
