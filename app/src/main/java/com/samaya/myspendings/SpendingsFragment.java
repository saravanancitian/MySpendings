package com.samaya.myspendings;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samaya.myspendings.db.entity.MonthlyOrYearlySpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpendingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpendingsFragment extends Fragment {

    public static final int FRAGMENT_TYPE_DAILY = 1;
    public static final int FRAGMENT_TYPE_MONTHLY = 2;
    public static final int FRAGMENT_TYPE_YEARlY = 3;
    private static final String ARG_FRAGMENT_TYPE = "param1";
    private int fragmentType;
    private SpendingsViewModel viewModel;




    public SpendingsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SpendingsFragment newInstance(int fragmenttype) {
        SpendingsFragment fragment = new SpendingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_TYPE, fragmenttype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentType = getArguments().getInt(ARG_FRAGMENT_TYPE);
        }
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_spendings, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView txtEmpty = (TextView) rootView.findViewById(R.id.txt_emtpy);

        switch (fragmentType){
            case FRAGMENT_TYPE_DAILY:{

                DailySpendingsAdapter dailySpendingAdapter = new DailySpendingsAdapter(inflater);
                mRecyclerView.setAdapter(dailySpendingAdapter);
                dailySpendingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if(dailySpendingAdapter.getItemCount() > 0){
                            if(txtEmpty.getVisibility() == View.VISIBLE){
                                txtEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });


                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        viewModel.delete(dailySpendingAdapter.getItem(position));
                        dailySpendingAdapter.removeItem(position);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                View itemView = viewHolder.itemView;
//                int itemHeight = itemView.getBottom() - itemView.getTop();
//                Drawable background  = new ColorDrawable();
//                ((ColorDrawable) background).setColor(Color.RED);
//                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//                background.draw(c);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                });
                itemTouchHelper.attachToRecyclerView(mRecyclerView);

                LiveData<List<Spendings>> spendings = viewModel.getAllspendings();
                if(spendings.getValue() == null || spendings.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendings.observe(getViewLifecycleOwner(), new Observer<List<Spendings>>() {
                    @Override
                    public void onChanged(@Nullable final List<Spendings> spendings) {
                        // Update the cached copy of the words in the adapter.
                        dailySpendingAdapter.setSpendingsList(spendings);
                    }
                });

                dailySpendingAdapter.setOnItemClickListener(new DailySpendingsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Spendings spending) {
                        Intent intent = new Intent(getActivity(), RecordActivity.class);
                        intent.putExtra("ops", "update");
                        intent.putExtra("ID", spending.ID);
                        intent.putExtra("amount",  spending.amount);
                        intent.putExtra("paidto", spending.paidto);
                        intent.putExtra("remark", spending.remark);
                        intent.putExtra("whendate", Utils.sdf.format(spending.whendt));
                        intent.putExtra("whentime", Utils.stf.format(spending.whendt));
                        startActivity(intent);
                    }
                });

            }break;
            case FRAGMENT_TYPE_MONTHLY:{
                MonthlyOrYearlySpendingsAdapter monthlySpendingsAdapter = new MonthlyOrYearlySpendingsAdapter(MonthlyOrYearlySpendingsAdapter.TYPE_MONTHLY,inflater);
                mRecyclerView.setAdapter(monthlySpendingsAdapter);
                monthlySpendingsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if(monthlySpendingsAdapter.getItemCount() > 0){
                            if(txtEmpty.getVisibility() == View.VISIBLE){
                                txtEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });

                LiveData<List<MonthlyOrYearlySpending>> spendings = viewModel.getMonthlySpendings();
                if(spendings.getValue() == null || spendings.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendings.observe(getViewLifecycleOwner(), new Observer<List<MonthlyOrYearlySpending>>() {
                    @Override
                    public void onChanged(@Nullable final List<MonthlyOrYearlySpending> spendings) {
                        // Update the cached copy of the words in the adapter.
                        monthlySpendingsAdapter.setMonthlySpendingsList(spendings);
                    }
                });

            }break;
            case FRAGMENT_TYPE_YEARlY:{
                MonthlyOrYearlySpendingsAdapter yearlySpendingsAdapter = new MonthlyOrYearlySpendingsAdapter(MonthlyOrYearlySpendingsAdapter.TYPE_YEARLY,inflater);
                mRecyclerView.setAdapter(yearlySpendingsAdapter);
                yearlySpendingsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if(yearlySpendingsAdapter.getItemCount() > 0){
                            if(txtEmpty.getVisibility() == View.VISIBLE){
                                txtEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });

                LiveData<List<MonthlyOrYearlySpending>> spendings = viewModel.getYearlySpendings();
                if(spendings.getValue() == null || spendings.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendings.observe(getViewLifecycleOwner(), new Observer<List<MonthlyOrYearlySpending>>() {
                    @Override
                    public void onChanged(@Nullable final List<MonthlyOrYearlySpending> spendings) {
                        // Update the cached copy of the words in the adapter.
                        yearlySpendingsAdapter.setMonthlySpendingsList(spendings);
                    }
                });

            }break;
        }
        return rootView;
    }
}