package io.mobile.citylocationviewer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.mobile.citylocationviewer.R;

public class MainActivity extends AppCompatActivity {


    private final String TAG_CITIES_FRAGMENT = "TAG_CITIES_FRAGMENT";

    {
        System.out.println("create class");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createRootView());

        FragmentManager fm = getSupportFragmentManager();
        Fragment retainedFragment = fm.findFragmentByTag(TAG_CITIES_FRAGMENT);

        if (retainedFragment == null) {
            retainedFragment = CitiesFragment.newInstance();
            fm.beginTransaction().add(R.id.idRoot, retainedFragment, TAG_CITIES_FRAGMENT).commit();
        }
    }

    private View createRootView() {
        View root = new FrameLayout(this);
        root.setId(R.id.idRoot);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return root;
    }
}
