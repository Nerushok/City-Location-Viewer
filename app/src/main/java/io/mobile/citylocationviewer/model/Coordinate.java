package io.mobile.citylocationviewer.model;

import com.google.gson.annotations.SerializedName;

public class Coordinate {

    @SerializedName("lon") private float longitude;
    @SerializedName("lat") private float latitude;

    public Coordinate(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}
