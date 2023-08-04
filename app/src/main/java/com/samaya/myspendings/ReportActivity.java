package com.samaya.myspendings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private SpendingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));
        LineChart chart = (LineChart) findViewById(R.id.chart);

        LiveData<List<Spendings>> spendings = viewModel.getDailySpendingsForReport();
           spendings.observe(this, new Observer<List<Spendings>>() {
            @Override
            public void onChanged(@Nullable final List<Spendings> spendings) {
                // Update the cached copy of the words in the adapter.
                List<Entry> entries = new ArrayList<Entry>();
                for(int i = 0 ; i < spendings.size(); i++){
                    entries.add(new Entry(i, spendings.get(i).amount));
                }

                LineDataSet dataSet = new LineDataSet(entries,"All Transcations");
                LineData data = new LineData(dataSet);
                chart.setData(data);
                chart.invalidate();
            }
        });

    }
}