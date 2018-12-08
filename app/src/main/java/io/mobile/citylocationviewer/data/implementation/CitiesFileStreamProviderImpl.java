package io.mobile.citylocationviewer.data.implementation;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import io.mobile.citylocationviewer.data.CitiesFileStreamProvider;

public class CitiesFileStreamProviderImpl implements CitiesFileStreamProvider {


    private final Context context;


    public CitiesFileStreamProviderImpl(Context context) {
        this.context = context;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return context.getAssets().open("cities.json");
        } catch (IOException e) {
            return null;
        }
    }
}
