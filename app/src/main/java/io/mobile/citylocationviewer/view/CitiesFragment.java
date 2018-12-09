package io.mobile.citylocationviewer.view;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.mobile.citylocationviewer.R;
import io.mobile.citylocationviewer.data.CitiesRepository;
import io.mobile.citylocationviewer.data.implementation.CitiesFileStreamProviderImpl;
import io.mobile.citylocationviewer.data.implementation.CitiesRepositoryImpl;
import io.mobile.citylocationviewer.model.City;
import io.mobile.citylocationviewer.view.adapter.CitiesAdapter;

public class CitiesFragment extends Fragment {


    private final String TAG = this.getClass().getSimpleName();

    private SearchView searchView;
    private RecyclerView citiesList;
    private View emptyView;
    private View progressView;
    private ProgressDialog loadingDialog;

    private CitiesAdapter citiesAdapter;
    private final CitiesAdapter.OnCityClickListener onCityClickListener = new CitiesAdapter.OnCityClickListener() {
        @Override
        public void onClick(City city) {
            Log.d(TAG, "Click on item: " + city.getName());
        }
    };

    private CitiesRepository citiesRepository;

    private SearchCitiesTask loadCitiesByQueryTask;

    private String lastQuery;


    public static CitiesFragment newInstance() {
        return new CitiesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initRetainedData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        initViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loadCitiesByQueryTask != null) loadCitiesByQueryTask.cancel(true);
    }

    private void initRetainedData() {
        citiesAdapter = new CitiesAdapter();
        citiesAdapter.setHasStableIds(true);
        citiesAdapter.setOnCityClickListener(onCityClickListener);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Loading...");

        citiesRepository = createCitiesRepository();
        initRepository();
    }

    private void findViews() {
        searchView = getView().findViewById(R.id.search_view);
        citiesList = getView().findViewById(R.id.list_city);
        emptyView = getView().findViewById(R.id.empty_view);
        progressView = getView().findViewById(R.id.progress_view);
    }

    private void initViews() {
        citiesList.setLayoutManager(new LinearLayoutManager(getContext()));
        citiesList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        citiesList.setItemAnimator(null);
        citiesList.setAdapter(citiesAdapter);
        citiesList.setHasFixedSize(true);
        citiesList.setItemViewCacheSize(20);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchCities(s);
                return false;
            }
        });
    }

    private CitiesRepository createCitiesRepository() {
        return new CitiesRepositoryImpl(new CitiesFileStreamProviderImpl(getContext()));
    }

    private void initRepository() {
        Log.d(TAG, "initRepository: ");
        new InitRepositoryTask().execute();
    }

    private void searchCities(@Nullable String query) {
        String newQuery = query != null ? query.trim() : null;

        if (newQuery != null && newQuery.equals(lastQuery)) return;

        lastQuery = query;

        searchCitiesInternal();
    }

    private void searchCitiesInternal() {
        if (loadCitiesByQueryTask != null) loadCitiesByQueryTask.cancel(true);

        loadCitiesByQueryTask = new SearchCitiesTask(lastQuery);
        loadCitiesByQueryTask.execute();
    }

    private void setDataToList(List<City> cities) {
        if (cities.isEmpty()) {
            setViewVisibility(emptyView, true);
        } else {
            citiesAdapter.setData(cities);
            setViewVisibility(citiesList, true);
        }
    }

    private void setLoading(boolean loading) {
        if (loading) {
            setViewVisibility(emptyView, false);
            setViewVisibility(citiesList, false);
        }
        setViewVisibility(progressView, loading);
    }

    private void setViewVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    public class InitRepositoryTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "init repo");
            loadingDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            citiesRepository.init();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingDialog.hide();
            searchCities(null);
        }
    }

    public class SearchCitiesTask extends AsyncTask<Void, Void, List<City>> {

        @Nullable private final String query;

        SearchCitiesTask(String query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setLoading(true);
        }

        @Override
        protected List<City> doInBackground(Void... args) {
            return citiesRepository.searchByPrefix(query);
        }

        @Override
        protected void onPostExecute(List<City> cities) {
            super.onPostExecute(cities);
            loadCitiesByQueryTask = null;
            setDataToList(cities);
            setLoading(false);
        }

        @Override
        protected void onCancelled() {
            loadCitiesByQueryTask = null;
            setLoading(false);
        }
    }
}
