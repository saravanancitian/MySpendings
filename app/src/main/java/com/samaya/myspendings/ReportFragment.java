package com.samaya.myspendings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;
import com.samaya.myspendings.db.entity.DMYSpending;
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
    private ReportViewModel mViewModel;


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
        RecyclerView recyclerView = reportview.findViewById(R.id.reportrecyclerview);
        MaterialButton btnDateRange = reportview.findViewById(R.id.btn_date_range_picker);
        btnDateRange.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        switch(fragmentType){
            case FRAGMENT_REPORT_TYPE_ALL_SPENDINGS:{
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

            }break;

            case FRAGMENT_REPORT_TYPE_MONTHLY:{
                recyclerView.setVisibility(View.VISIBLE);
                LiveData<List<String>> allmonths =  mViewModel.getAllmonths();
                 allmonths.observe(getViewLifecycleOwner(), new Observer<List<String>>(){

                    @Override
                    public void onChanged(List<String> strings) {
                        ReportRVListAdapter adapter = new ReportRVListAdapter((String[]) strings.toArray(), getLayoutInflater());

                    }
                });


            }break;

            case FRAGMENT_REPORT_TYPE_YEARlY:{
                recyclerView.setVisibility(View.VISIBLE);

            }break;

            case FRAGMENT_REPORT_TYPE_DATERANGE:{
                btnDateRange.setVisibility(View.VISIBLE);

            }break;
        }



        return reportview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentType = getArguments().getInt(ARG_FRAGMENT_TYPE);
        }
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);


        // TODO: Use the ViewModel
    }

}