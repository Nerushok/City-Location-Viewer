package io.mobile.citylocationviewer.model;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("_id") private Long id;
    @SerializedName("name") private String name;
    @SerializedName("country") private String country;
    @SerializedName("coord") private Coordinate coordinate;

    public City(Long id, String name, String country, Coordinate coordinate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coordinate = coordinate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
