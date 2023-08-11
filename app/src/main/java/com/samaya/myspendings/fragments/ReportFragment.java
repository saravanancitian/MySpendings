package com.samaya.myspendings.fragments;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.samaya.myspendings.R;
import com.samaya.myspendings.utils.Utils;
import com.samaya.myspendings.adapters.DateListAdapter;
import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        View reportview = null;



        switch(fragmentType){
            case FRAGMENT_REPORT_TYPE_ALL_SPENDINGS:{
                reportview = inflater.inflate(R.layout.fragment_report_all, container, false);
                LineChart chart = (LineChart) reportview.findViewById(R.id.chart_all);

                LiveData<List<Spendings>> spendings = mViewModel.getDailySpendingsForReport();

                spendings.observe(getViewLifecycleOwner(), spendings1 -> {
                    // Update the cached copy of the words in the adapter.
                    List<String> dates = new ArrayList<>();
                    List<Entry> entries = new ArrayList<Entry>();
                    for(int i = 0; i < spendings1.size(); i++){
                        entries.add(new Entry(i, spendings1.get(i).amount));
                        dates.add(Utils.rdf.format(spendings1.get(i).whendt));
                    }

                    ValueFormatter formatter = new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return dates.get((int)value);
                        }
                    };

                    LineDataSet dataSet = new LineDataSet(entries,"All Transcations");
                    LineData data = new LineData(dataSet);
                    chart.setData(data);
                    XAxis axis = chart.getXAxis();
                    axis.setValueFormatter(formatter);
                    chart.invalidate();

                });

            }break;

            case FRAGMENT_REPORT_TYPE_MONTHLY:{
                reportview = inflater.inflate(R.layout.fragment_report_date, container, false);
                LineChart chart = (LineChart) reportview.findViewById(R.id.chart_date);
                RecyclerView recyclerView = reportview.findViewById(R.id.reportrecyclerview);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setLayoutManager(mLayoutManager);
                DateListAdapter dateListAdapter = new DateListAdapter(DateListAdapter.DATE_LIST_ADAPTER_TYPE_MONTH_YEAR, getLayoutInflater());
                LiveData<List<String>> allmonths =  mViewModel.getAllmonths();


                allmonths.observe(getViewLifecycleOwner(), strings -> {
                    dateListAdapter.setDates(strings);
                    recyclerView.setAdapter(dateListAdapter);
                });

                dateListAdapter.setOnItemClickListener((view, position, str)->{
                    LiveData<List<DMYSpending>> monthsdata = mViewModel.getDailyTotalForMonth(str);
                    monthsdata.observe(getViewLifecycleOwner(), datalist->{
                        List<Entry> entries = new ArrayList<Entry>();
                        List<String> dates = new ArrayList<>();
                        for(int i = 0; i < datalist.size(); i++){
                            entries.add(new Entry(i, datalist.get(i).amount));
                            dates.add(datalist.get(i).dmyDate);
                        }

                        ValueFormatter formatter = new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                return dates.get((int)value);
                            }
                        };

                        LineDataSet dataSet = new LineDataSet(entries,"Monthly Spendings");
                        LineData data = new LineData(dataSet);
                        chart.setData(data);
                        XAxis axis = chart.getXAxis();
                        axis.setValueFormatter(formatter);
                        chart.invalidate();
                    });
                });

                Calendar calendar = Calendar.getInstance();

                LiveData<List<DMYSpending>> monthsdata = mViewModel.getDailyTotalForMonth(Utils.monthyearformat.format(calendar.getTime()));
                monthsdata.observe(getViewLifecycleOwner(), datalist->{
                    List<Entry> entries = new ArrayList<Entry>();
                    List<String> dates = new ArrayList<>();
                    for(int i = 0; i < datalist.size(); i++){
                        entries.add(new Entry(i, datalist.get(i).amount));
                        dates.add(datalist.get(i).dmyDate);
                    }

                    ValueFormatter formatter = new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            return dates.get((int)value);
                        }
                    };
                    LineDataSet dataSet = new LineDataSet(entries,"Monthly Spendings");
                    LineData data = new LineData(dataSet);
                    chart.setData(data);
                    XAxis axis = chart.getXAxis();
                    axis.setValueFormatter(formatter);
                    chart.invalidate();
                });



            }break;

            case FRAGMENT_REPORT_TYPE_YEARlY:{
                reportview = inflater.inflate(R.layout.fragment_report_date, container, false);
                LineChart chart = (LineChart) reportview.findViewById(R.id.chart_date);
                RecyclerView recyclerView = reportview.findViewById(R.id.reportrecyclerview);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setLayoutManager(mLayoutManager);
                DateListAdapter dateListAdapter = new DateListAdapter(DateListAdapter.DATE_LIST_ADAPTER_TYPE_YEAR, getLayoutInflater());
                LiveData<List<String>> allyears =  mViewModel.getAllyears();
                allyears.observe(getViewLifecycleOwner(), strings -> {
                    dateListAdapter.setDates(strings);
                    recyclerView.setAdapter(dateListAdapter);

                });

                dateListAdapter.setOnItemClickListener((view, position, str)->{
                    LiveData<List<DMYSpending>> yeardata = mViewModel.getMonthlyTotalForYearForReport(str);
                    yeardata.observe(getViewLifecycleOwner(), datalist->{
                        List<Entry> entries = new ArrayList<Entry>();
                        for(int i = 0; i < datalist.size(); i++){
                            entries.add(new Entry(i, datalist.get(i).amount));
                        }

                        LineDataSet dataSet = new LineDataSet(entries,"Yearly Spendings");
                        LineData data = new LineData(dataSet);
                        chart.setData(data);
                        chart.invalidate();
                    });
                });
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                LiveData<List<DMYSpending>> yeardata = mViewModel.getMonthlyTotalForYearForReport(String.valueOf(year));
                yeardata.observe(getViewLifecycleOwner(), datalist->{
                    List<Entry> entries = new ArrayList<Entry>();
                    for(int i = 0; i < datalist.size(); i++){
                        entries.add(new Entry(i, datalist.get(i).amount));
                    }

                    LineDataSet dataSet = new LineDataSet(entries,"Yearly Spendings");
                    LineData data = new LineData(dataSet);
                    chart.setData(data);
                    chart.invalidate();
                });



            }break;

            case FRAGMENT_REPORT_TYPE_DATERANGE:{
                reportview = inflater.inflate(R.layout.fragment_report_range, container, false);
                LineChart chart = (LineChart) reportview.findViewById(R.id.chart_range);
                MaterialButton btnDateRange = reportview.findViewById(R.id.btn_date_range_picker);
                Calendar calendar = Calendar.getInstance();

                MaterialDatePicker<Pair<Long,Long>> materialDatePicker;
                MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
                materialDateBuilder.setTitleText("SELECT A DATE RANGE");
                materialDatePicker = materialDateBuilder.build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        Date first =  new Date(selection.first);
                        Date sec = new Date(selection.second);
                        LiveData<List<Spendings>> rangeSpendings = mViewModel.getAllSpendingsInRangeForReport(first,sec);
                        rangeSpendings.observe(getViewLifecycleOwner(), new Observer<List<Spendings>>() {
                            @Override
                            public void onChanged(List<Spendings> spendings) {
                                List<Entry> entries = new ArrayList<Entry>();
                                List<String> dates = new ArrayList<>();

                                for(int i = 0; i < spendings.size(); i++){
                                    entries.add(new Entry(i, spendings.get(i).amount));
                                    dates.add(Utils.rdf.format(spendings.get(i).whendt));
                                }

                                ValueFormatter formatter = new ValueFormatter() {
                                    @Override
                                    public String getAxisLabel(float value, AxisBase axis) {
                                        return dates.get((int)value);
                                    }
                                };

                                LineDataSet dataSet = new LineDataSet(entries,"Range Transcations");
                                LineData data = new LineData(dataSet);
                                chart.setData(data);
                                XAxis axis = chart.getXAxis();
                                axis.setValueFormatter(formatter);
                                chart.invalidate();
                            }
                        });
                    }
                });

                btnDateRange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDatePicker.show(getActivity().getSupportFragmentManager(), "SELECT_A_DATE_RANGE");
                    }
                });

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