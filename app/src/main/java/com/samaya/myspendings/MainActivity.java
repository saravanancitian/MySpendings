package com.samaya.myspendings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private SpendingsViewModel viewModel;

    private SpendingsFragment dailySpendingFragment;
    private SpendingsFragment monthlySpendingFragment;
//    private SpendingsFragment yearlSpendingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

        dailySpendingFragment = SpendingsFragment.newInstance(SpendingsFragment.FRAGMENT_TYPE_DAILY);
        monthlySpendingFragment = SpendingsFragment.newInstance(SpendingsFragment.FRAGMENT_TYPE_MONTHLY);

        FloatingActionButton fab = findViewById(R.id.fab);
        TextInputEditText txtTotalSpendings = findViewById(R.id.txt_totalspendings);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.d("Spending", "-----------------tabselected------------"+tab.getId() +" pos " + tab.getPosition());
                Log.d("Spending", "tabdaily"+ R.id.tabdaily +" tabmonthly "+ R.id.tabmonthly);

                switch(tab.getPosition()){
                    case 0:{
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, dailySpendingFragment, null)
                                .commit();
                    }break;
                    case 1:{
                        getSupportFragmentManager().beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.fragment_container_view, monthlySpendingFragment, null)
                                .commit();
                    }break;
                    case 2:{
                        //                    getSupportFragmentManager().beginTransaction()
//                            .setReorderingAllowed(true)
//                            .add(R.id.fragment_container_view, dailySpendingFragment, null)
//                            .commit();
                    }break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewModel.getTotalspendings().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer totalSpendings) {
                // Update the cached copy of the words in the adapter.
                if(totalSpendings != null && totalSpendings > 0) {
                    txtTotalSpendings.setText(String.valueOf(totalSpendings));
                } else {
                    txtTotalSpendings.setText("");
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start RecordActivity
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, dailySpendingFragment, null)
                    .commit();
        }

    }


}