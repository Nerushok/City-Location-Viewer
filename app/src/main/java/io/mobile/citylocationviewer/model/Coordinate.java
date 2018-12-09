package io.mobile.citylocationviewer.model;

import com.google.gson.annotations.SerializedName;

public class Coordinate {

    @SerializedName("lon") private final float longitude;
    @SerializedName("lat") private final float latitude;

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
