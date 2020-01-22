package com.itonemm.clientapponline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaDataSource;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class PlayVideoActivity extends AppCompatActivity {

    SharedRef ref;

    RewardedAd mInterstitialAd;
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer player;
    ProgressBar loading;
    public static String videourl;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        activity=this;
        new MyTask().execute();

        ref=new SharedRef(getApplicationContext());

        exoPlayerView=findViewById(R.id.exoplayerview);
        loading=findViewById(R.id.videoloadingview);
        new LoadingVideo().execute();







    }

    private class LoadingVideo extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            player= ExoPlayerFactory.newSimpleInstance(getApplicationContext(),new DefaultTrackSelector());
            DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("exo player");
            DefaultExtractorsFactory ef=new DefaultExtractorsFactory();
            Uri videouri=Uri.parse(videourl);
            ExtractorMediaSource mediaSource=new ExtractorMediaSource(videouri,dataSourceFactory,ef,null,null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            TrackSelector selctore=new DefaultTrackSelector();
            player.seekTo(0);
            ref.savePosiition(0);
            SimpleExoPlayer.EventListener listener=new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if(playbackState==SimpleExoPlayer.STATE_BUFFERING){
                        loading.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        loading.setVisibility(View.GONE);
                        if(mInterstitialAd.isLoaded() && player.getPlayWhenReady())
                        {

                            RewardedAdCallback adCallback = new RewardedAdCallback() {
                                @Override
                                public void onRewardedAdOpened() {
                                    player.setPlayWhenReady(false);
                                    player.seekTo(player.getCurrentPosition());
                                    // Ad opened.
                                }

                                @Override
                                public void onRewardedAdClosed() {

                                    // Ad closed.
                                }

                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem reward) {

                                    player.seekTo(player.getCurrentPosition());
                                    player.setPlayWhenReady(true);

                                }

                                @Override
                                public void onRewardedAdFailedToShow(int errorCode) {
                                    // Ad failed to display.
                                }

                            };
                            mInterstitialAd.show(activity,adCallback);
                            player.setPlayWhenReady(!player.getPlayWhenReady());


                        }
                        else
                        {
                            player.seekTo(player.getCurrentPosition());
                            player.setPlayWhenReady(true);

                        }

                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            };
            player.addListener(listener);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            exoPlayerView.setPlayer(player);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
        ref.savePosiition(0);
    }

    @Override
    protected void onPause() {

        super.onPause();

        player.setPlayWhenReady(!player.getPlayWhenReady());
        player.seekTo(player.getCurrentPosition());
        ref.savePosiition((int)player.getContentPosition());

    }

    class MyTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.app_id));
                    mInterstitialAd= new RewardedAd(getApplicationContext(),
                            getResources().getString(R.string.rewareded_id));
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
                    mInterstitialAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }





    @Override
    protected void onStop() {

        super.onStop();
        player.stop();
        exoPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(player.getCurrentPosition());
        ref.savePosiition((int)player.getCurrentPosition());
    }
}
