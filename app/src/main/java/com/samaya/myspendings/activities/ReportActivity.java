package com.samaya.myspendings.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.samaya.myspendings.R;
import com.samaya.myspendings.adapters.ReportFragmentAdapter;

public class ReportActivity extends AppCompatActivity {

    private ViewPager2 viewPager;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        viewPager = findViewById(R.id.reportpager);
        FragmentStateAdapter pagerAdapter = new ReportFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        TabLayout tabLayout = findViewById(R.id.reporttablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_all_spendings));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_monthly));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_yearly));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_daterange));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}