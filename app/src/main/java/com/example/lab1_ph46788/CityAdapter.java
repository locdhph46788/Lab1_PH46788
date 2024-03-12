package com.example.lab1_ph46788;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CityDTO> listCity;
    private HomeActivity homeActivity;

    public CityAdapter(Context context, ArrayList<CityDTO> listCity, HomeActivity homeActivity) {
        this.context = context;
        this.listCity = listCity;
        this.homeActivity = homeActivity;
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        CityDTO city = listCity.get(position);
        holder.tvStt.setText(String.valueOf(holder.getAdapterPosition() + 1));
        holder.tvName.setText(city.getName());
        holder.tvPopulation.setText(String.valueOf(city.getPopulation()));
        holder.tvCountry.setText(city.getCountry());
    }

    @Override
    public int getItemCount() {
        return listCity != null ? listCity.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStt, tvName, tvPopulation, tvCountry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStt = itemView.findViewById(R.id.tv_stt);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPopulation = itemView.findViewById(R.id.tv_population);
            tvCountry = itemView.findViewById(R.id.tv_country);
        }
    }
}
