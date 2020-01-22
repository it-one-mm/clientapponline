package com.itonemm.clientapponline;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SeriesDetailsActivity extends AppCompatActivity {
    AdView mAdView1,mAdView2;
    private InterstitialAd mInterstitialAd;
    static SeriesModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_details);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        ImageView imageView=findViewById(R.id.series_image);
        TextView seriesName=findViewById(R.id
        .series_name);
        Glide.with(getApplicationContext())
                .load(model.seriesImage)
                .into(imageView);
        seriesName.setText(model.seriesName);
         mAdView1 = findViewById(R.id.adView1);
       mAdView2 =findViewById(R.id.adView2);
       new Loading().execute();

        EpisodeRecyclerAdapter.activity=this;
        final RecyclerView episode_list=findViewById(R.id.episode_list);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference movieRef=db.collection("movies");
        movieRef.whereEqualTo("movieSeries",model.seriesName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MovieModel> episodes=new ArrayList<>();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    episodes.add(snapshot.toObject(MovieModel.class));
                }
                EpisodeRecyclerAdapter adapter=new EpisodeRecyclerAdapter(episodes,getApplicationContext());
                LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                episode_list.setAdapter(adapter);
                episode_list.setLayoutManager(lm);

            }
        });
        loadAds();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();;
                }
            }
        },5000);
    }
    private  class Loading extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView1.loadAd(adRequest);


                    adRequest = new AdRequest.Builder().build();
                    mAdView2.loadAd(adRequest);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public void loadAds()
    {
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
