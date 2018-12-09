package io.mobile.citylocationviewer.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.mobile.citylocationviewer.R;
import io.mobile.citylocationviewer.model.City;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {


    private final List<City> cities = new ArrayList<>();
    private OnCityClickListener onCityClickListener;


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false);
        return new CityViewHolder(view, onCityClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder cityViewHolder, int position) {
        cityViewHolder.bindData(cities.get(position));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CityViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.setOnClickListener();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CityViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.removeOnClickListener();
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    @Override
    public long getItemId(int position) {
        return cities.get(position).getId();
    }

    public void setData(List<City> newData) {
        cities.clear();
        cities.addAll(newData);
        notifyDataSetChanged();
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        onCityClickListener = listener;
    }


    static class CityViewHolder extends RecyclerView.ViewHolder {


        private City city;
        private final StringBuilder stringBuilder = new StringBuilder();
        private final OnCityClickListener onCityClickListener;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityClickListener == null) return;

                onCityClickListener.onClick(city);
            }
        };


        CityViewHolder(@NonNull View itemView, OnCityClickListener onCityClickListener) {
            super(itemView);
            this.onCityClickListener = onCityClickListener;
        }

        void bindData(City city) {
            this.city = city;
            ((TextView) itemView).setText(getCityFullName(city));
        }

        void setOnClickListener() {
            itemView.setOnClickListener(onClickListener);
        }

        void removeOnClickListener() {
            itemView.setOnClickListener(null);
        }

        private String getCityFullName(City city) {
            stringBuilder.delete(0, stringBuilder.length());
            return stringBuilder
                    .append(city.getName())
                    .append(", ")
                    .append(city.getCountry())
                    .toString();
        }
    }

    public interface OnCityClickListener {

        void onClick(City city);
    }
}
