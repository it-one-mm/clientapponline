package com.itonemm.clientapponline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {
    private RewardedAd rewardedAd;
    ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
    Context context;
    static Activity activity;
    LayoutInflater inflater;

    public MovieRecyclerAdapter(ArrayList<MovieModel> movieModels, Context context, LayoutInflater inflater) {
        this.movieModels = movieModels;
        this.context = context;
        this.inflater = inflater;
        MobileAds.initialize(context,context.getResources().getString(R.string.app_id));
        rewardedAd = new RewardedAd(context,context.getResources().getString(R.string.rewareded_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, final int position) {


        Glide.with(context)
                .load(movieModels.get(position).movieImage)
                .into(holder.itemimage);
        holder.itemname.setText(movieModels.get(position).movieName);
        holder.itemimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Loading().execute(movieModels.get(position).movieVideo);

            }
        });
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

    public String  videolink;
    private class Loading extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPostExecute(final String s) {

            super.onPostExecute(s);
            if(rewardedAd.isLoaded()){
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        Intent intent=new Intent(context,PlayVideoActivity.class);

                        try {
                            PlayVideoActivity.videourl=s;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onRewardedAdFailedToShow(int errorCode) {
                        // Ad failed to display.
                    }
                };
                rewardedAd.show(activity, adCallback);
            }
            else{
                Intent intent=new Intent(context,PlayVideoActivity.class);

                try {
                    PlayVideoActivity.videourl=s;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);}
        }

        @Override
        protected String doInBackground(String... strings) {

            String link="";
            try {
                  link=MediaFireConnect.getVideoFileLink(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return link;
        }
    }
}
