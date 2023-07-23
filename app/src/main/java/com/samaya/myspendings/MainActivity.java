package com.samaya.myspendings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private SpendingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

        FloatingActionButton fab = findViewById(R.id.fab);
        MaterialTextView txtTotalSpendings = findViewById(R.id.txt_totalspendings);

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
                    .add(R.id.fragment_container_view, SpendingsFragment.class, null)
                    .commit();
        }

    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        if(item.getItemId() == R.id.recorditm){
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .replace(R.id.fragment_container_view, RecordFragment.class, null)
//                    .commit();
//            return true;
//        } else if(item.getItemId() == R.id.spendingitm){
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .replace(R.id.fragment_container_view, SpendingsFragment.class, null)
//                    .commit();
//            return true;
//
//        }
//        return false;
//    }
}