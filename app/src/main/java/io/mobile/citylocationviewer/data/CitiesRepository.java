package io.mobile.citylocationviewer.data;

import java.util.List;

import io.mobile.citylocationviewer.model.City;

public interface CitiesRepository {

    void init();

    List<City> searchByPrefix(String prefix);
}
