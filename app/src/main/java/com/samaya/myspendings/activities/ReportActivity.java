package com.samaya.myspendings.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;
import com.samaya.myspendings.R;
import com.samaya.myspendings.adapters.ReportFragmentAdapter;

public class ReportActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        viewPager = findViewById(R.id.reportpager);
        pagerAdapter = new ReportFragmentAdapter(getSupportFragmentManager(), getLifecycle());
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
}