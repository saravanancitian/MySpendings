package com.samaya.myspendings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private static final String ARG_FRAGMENT_TYPE = "param1";

    public static final int FRAGMENT_REPORT_TYPE_ALL_SPENDINGS = 0;
    public static final int FRAGMENT_REPORT_TYPE_MONTHLY = 1;
    public static final int FRAGMENT_REPORT_TYPE_YEARlY = 2;
    public static final int FRAGMENT_REPORT_TYPE_DATERANGE = 3;

    public static final int FRAGMENT_REPORT_TYPE_COUNT = 4;


    private int fragmentType;
    private SpendingsViewModel mViewModel;

    public static ReportFragment newInstance(int fragmenttype) {

       ReportFragment reportFragment =  new ReportFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_TYPE, fragmenttype);
        reportFragment.setArguments(args);

        return reportFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View reportview = inflater.inflate(R.layout.fragment_report, container, false);

        LineChart chart = (LineChart) reportview.findViewById(R.id.chart);
        LiveData<List<Spendings>> spendings = mViewModel.getDailySpendingsForReport();
        spendings.observe(getViewLifecycleOwner(), new Observer<List<Spendings>>() {
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


        return reportview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentType = getArguments().getInt(ARG_FRAGMENT_TYPE);
        }
        mViewModel = new ViewModelProvider(this).get(SpendingsViewModel.class);


        // TODO: Use the ViewModel
    }

}