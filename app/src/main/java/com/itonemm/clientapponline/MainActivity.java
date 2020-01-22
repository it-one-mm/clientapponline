package com.itonemm.clientapponline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.TokenWatcher;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout=findViewById(R.id.tablayout);
        ViewPager pager=findViewById(R.id.viewpager);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.app_id));
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(),"Movies");
        adapter.addFragment(new SeriesFragment(),"Series");

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        loadAds();;
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

    public void loadAds()
    {
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }



}
