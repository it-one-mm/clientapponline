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

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {

    ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
    Context context;
    LayoutInflater inflater;

    public MovieRecyclerAdapter(ArrayList<MovieModel> movieModels, Context context, LayoutInflater inflater) {
        this.movieModels = movieModels;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        Glide.with(context)
                .load(movieModels.get(position).movieImage)
                .into(holder.itemimage);
        holder.itemname.setText(movieModels.get(position).movieName);
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{

        ImageView itemimage;
        TextView itemname;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            itemimage=itemView.findViewById(R.id.item_image);
            itemname=itemView.findViewById(R.id.movie_name);
        }
    }

}
