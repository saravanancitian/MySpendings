package com.samaya.myspendings.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.samaya.myspendings.R;
import com.samaya.myspendings.adapters.SpendingsFragmentAdapter;
import com.samaya.myspendings.fragments.SpendingsViewModel;

public class MainActivity extends AppCompatActivity{

    private SpendingsViewModel viewModel;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new SpendingsFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        TextInputEditText txtTotalSpendings = findViewById(R.id.txt_totalspendings);

        MaterialButton btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });
        MaterialButton btnExport = findViewById(R.id.btn_export);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_all_spendings));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_daily));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_monthly));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.str_yearly));
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

        viewModel.getTotalspendings().observe(this, totalSpendings -> {
            // Update the cached copy of the words in the adapter.
            if(totalSpendings != null && totalSpendings > 0) {
                txtTotalSpendings.setText(String.valueOf(totalSpendings));
            } else {
                txtTotalSpendings.setText("");
            }
        });
        fab.setOnClickListener(view -> {
            //start RecordActivity
            startActivity(new Intent(MainActivity.this, RecordActivity.class));
        });
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

}