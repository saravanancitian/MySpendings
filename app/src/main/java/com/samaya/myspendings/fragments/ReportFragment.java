package com.samaya.myspendings.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.samaya.myspendings.R;
import com.samaya.myspendings.adapters.DateListAdapter;
import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.utils.DateUtils;

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
                LineChart chart = reportview.findViewById(R.id.chart_all);

                LiveData<List<Spendings>> spendings = mViewModel.getDailySpendingsForReport();

                spendings.observe(getViewLifecycleOwner(), spendings1 -> {
                    // Update the cached copy of the words in the adapter.
                    List<String> dates = new ArrayList<>();
                    List<Entry> entries = new ArrayList<>();
                    for(int i = 0; i < spendings1.size(); i++){
                        entries.add(new Entry(i, spendings1.get(i).amount));
                        dates.add(DateUtils.rdf.format(spendings1.get(i).whendt));
                    }


                    drawChart(chart, entries, new DateValueFormatter(dates), "All Transcations");

                });

            }break;

            case FRAGMENT_REPORT_TYPE_MONTHLY:{
                reportview = inflater.inflate(R.layout.fragment_report_date, container, false);
                LineChart chart = reportview.findViewById(R.id.chart_date);
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
                        List<Entry> entries = new ArrayList<>();
                        List<String> dates = new ArrayList<>();
                        for(int i = 0; i < datalist.size(); i++){
                            entries.add(new Entry(i, datalist.get(i).amount));
                            String date[] = datalist.get(i).dmyDate.split("-");
                            int num = Integer.parseInt(date[1]);

                            dates.add(date[0]+"-"+DateUtils.getShortMonths(num));
                        }


                        drawChart(chart, entries, new DateValueFormatter(dates), "Monthly Spendings");

                    });
                });

                Calendar calendar = Calendar.getInstance();

                LiveData<List<DMYSpending>> monthsdata = mViewModel.getDailyTotalForMonth(DateUtils.monthyearformat.format(calendar.getTime()));
                monthsdata.observe(getViewLifecycleOwner(), datalist->{
                    List<Entry> entries = new ArrayList<>();
                    List<String> dates = new ArrayList<>();
                    for(int i = 0; i < datalist.size(); i++){
                        entries.add(new Entry(i, datalist.get(i).amount));
                        String date[] = datalist.get(i).dmyDate.split("-");
                        int num = Integer.parseInt(date[1]);
                        dates.add(date[0]+"-"+DateUtils.getShortMonths(num));
                    }

                    drawChart(chart, entries, new DateValueFormatter(dates), "Monthly Spendings");

                });



            }break;

            case FRAGMENT_REPORT_TYPE_YEARlY:{
                reportview = inflater.inflate(R.layout.fragment_report_date, container, false);
                LineChart chart = reportview.findViewById(R.id.chart_date);
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
                        List<Entry> entries = new ArrayList<>();
                        List<String> months = new ArrayList<>();

                        for(int i = 0; i < datalist.size(); i++){
                            entries.add(new Entry(i, datalist.get(i).amount));
                            String monthyear[] = datalist.get(i).dmyDate.split("-");
                            int num = Integer.parseInt(monthyear[0]);
                            months.add(DateUtils.getShortMonths(num));

                        }
                        drawChart(chart, entries, new DateValueFormatter(months), "Yearly Spendings");
                    });
                });
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                LiveData<List<DMYSpending>> yeardata = mViewModel.getMonthlyTotalForYearForReport(String.valueOf(year));
                yeardata.observe(getViewLifecycleOwner(), datalist->{
                    List<Entry> entries = new ArrayList<>();
                    List<String> months = new ArrayList<>();
                    for(int i = 0; i < datalist.size(); i++){
                        entries.add(new Entry(i, datalist.get(i).amount));
                        String monthyear[] = datalist.get(i).dmyDate.split("-");
                        int num = Integer.parseInt(monthyear[0]);
                        months.add(DateUtils.getShortMonths(num));
                    }

                    drawChart(chart, entries, new DateValueFormatter(months), "Yearly Spendings");
                });



            }break;

            case FRAGMENT_REPORT_TYPE_DATERANGE:{
                reportview = inflater.inflate(R.layout.fragment_report_range, container, false);
                LineChart chart = reportview.findViewById(R.id.chart_range);
                MaterialButton btnDateRange = reportview.findViewById(R.id.btn_date_range_picker);

                MaterialDatePicker<Pair<Long,Long>> materialDatePicker;
                MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
                materialDateBuilder.setTitleText("SELECT A DATE RANGE");
                materialDatePicker = materialDateBuilder.build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date first =  new Date(selection.first);
                    Date sec = new Date(selection.second);
                    LiveData<List<Spendings>> rangeSpendings = mViewModel.getAllSpendingsInRangeForReport(first,sec);
                    rangeSpendings.observe(getViewLifecycleOwner(), spendings -> {
                        List<Entry> entries = new ArrayList<>();
                        List<String> dates = new ArrayList<>();

                        for(int i = 0; i < spendings.size(); i++){
                            entries.add(new Entry(i, spendings.get(i).amount));
                            dates.add(DateUtils.rdf.format(spendings.get(i).whendt));
                        }

                        drawChart(chart, entries, new DateValueFormatter(dates), "Range Transcations");
                    });
                });

                btnDateRange.setOnClickListener(view -> materialDatePicker.show(requireActivity().getSupportFragmentManager(), "SELECT_A_DATE_RANGE"));

            }break;
        }

        return reportview;
    }


    void drawChart(LineChart chart , List<Entry> entries , ValueFormatter formatter, String label){
        LineDataSet dataSet = new LineDataSet(entries,label);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setFillColor(0xff0000);
        dataSet.setDrawFilled(true);

        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.setVisibleXRangeMaximum(10);
        if(formatter != null) {
            XAxis axis = chart.getXAxis();
            axis.setValueFormatter(formatter);
        }
        chart.invalidate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentType = getArguments().getInt(ARG_FRAGMENT_TYPE);
        }
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
   }


   private static class DateValueFormatter extends ValueFormatter {

       List<String> datevalues;


       public DateValueFormatter(List<String> datevalues){
         this.datevalues = datevalues;
     }

       @Override
       public String getAxisLabel(float value, AxisBase axis) {
           int idx = (int)value;
           if(idx < datevalues.size())
                return datevalues.get((int)value);
           else
               return super.getAxisLabel(value, axis);
       }
   }

}