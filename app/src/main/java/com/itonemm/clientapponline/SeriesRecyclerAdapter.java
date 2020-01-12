package com.itonemm.clientapponline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SeriesRecyclerAdapter extends RecyclerView.Adapter<SeriesRecyclerAdapter.SeriesHolder> {

    ArrayList<SeriesModel> movieModels=new ArrayList<SeriesModel>();
    Context context;
    LayoutInflater inflater;

    public SeriesRecyclerAdapter(ArrayList<SeriesModel> movieModels, Context context, LayoutInflater inflater) {
        this.movieModels = movieModels;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new SeriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, int position) {

        Glide.with(context)
                .load(movieModels.get(position).seriesImage)
                .into(holder.itemimage);
        holder.itemname.setText(movieModels.get(position).seriesName);
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class SeriesHolder extends RecyclerView.ViewHolder{

        ImageView itemimage;
        TextView itemname;
        public SeriesHolder(@NonNull View itemView) {
            super(itemView);
            itemimage=itemView.findViewById(R.id.item_image);
            itemname=itemView.findViewById(R.id.movie_name);
        }
    }

}
