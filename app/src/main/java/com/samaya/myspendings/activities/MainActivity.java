package com.samaya.myspendings.activities;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.samaya.myspendings.R;
import com.samaya.myspendings.adapters.SpendingsFragmentAdapter;
import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.fragments.SpendingsViewModel;
import com.samaya.myspendings.utils.DateUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import androidx.core.splashscreen.SplashScreen;
public class MainActivity extends AppCompatActivity implements ActivityResultCallback<Uri> {
    public static final String TAG = "MainActivity";

    private SpendingsViewModel viewModel;

    private ViewPager2 viewPager;

    ActivityResultLauncher launcher;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private AdView adView;

    LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        progressIndicator = findViewById(R.id.progress_line);
        progressIndicator.hide();


        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.drawer_layout_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        launcher = registerForActivityResult(new ActivityResultContracts.CreateDocument("text/csv"), this);


        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

        viewPager = findViewById(R.id.pager);
        FragmentStateAdapter pagerAdapter = new SpendingsFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(item -> {
            boolean handled = false;
            if(item.getItemId() == R.id.nav_record){
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);

                handled = true;
            }else if(item.getItemId() == R.id.nav_report){
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
                handled = true;
            } else if(item.getItemId() == R.id.nav_export){
                String filename = "spending_" + DateUtils.filedtf.format(new Date()) + ".csv";
                launcher.launch(filename);
                handled = true;
            } else if(item.getItemId() == R.id.nav_help){
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                handled =  true;
            } else if(item.getItemId() == R.id.nav_exit){
                MainActivity.this.finish();
                handled =  true;
            }
            if(handled){
                drawerLayout.closeDrawers();
            }

            return handled;
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        TextInputEditText txtTotalSpendings = findViewById(R.id.txt_totalspendings);

//        MaterialButton btnReport = findViewById(R.id.btn_report);
//        btnReport.setOnClickListener(view->{
//            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
//            startActivity(intent);
//        });
//        MaterialButton btnExport = findViewById(R.id.btn_export);
//        btnExport.setOnClickListener(view -> {
//            String filename =(new StringBuilder("spending_")).append(DateUtils.filedtf.format(new Date())).append(".csv").toString();
//            launcher.launch(filename);
//        });

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
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
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
//        if (viewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//        }

        super.onBackPressed();
    }

    @Override
    public void onActivityResult(Uri result) {
        if(result != null) {
            progressIndicator.show();
            viewModel.getAllspendings().observe(this, spendings -> {
                if (spendings != null && !spendings.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(Spendings.getHeader());
                    builder.append('\n');
                    for (Spendings spending : spendings) {
                        builder.append(spending.toCsvString());
                        builder.append('\n');
                    }
                    saveFile(result, builder.toString());
                    progressIndicator.hide();
                }
            });
        }
    }

    void saveFile(Uri fileuri, String data){
        OutputStream os = null;

        try {
            os = getContentResolver().openOutputStream(fileuri);
            if(os != null) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(os != null)
                    os.close();
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if(item.getItemId() == R.id.nav_report){
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
