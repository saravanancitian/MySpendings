package com.samaya.myspendings.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.samaya.myspendings.R;
import com.samaya.myspendings.activities.RecordActivity;
import com.samaya.myspendings.utils.DateUtils;
import com.samaya.myspendings.adapters.AllSpendingsAdapter;
import com.samaya.myspendings.adapters.DMYSpendingsAdapter;
import com.samaya.myspendings.db.entity.DMYSpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;
import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpendingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpendingsFragment extends Fragment {

    public static final int FRAGMENT_TYPE_ALL_SPENDINGS = 1;
    public static final int FRAGMENT_TYPE_DAILY = 2;
    public static final int FRAGMENT_TYPE_MONTHLY = 3;
    public static final int FRAGMENT_TYPE_YEARlY = 4;

    public static final int TOTAL_FRAGMENT_COUNT = 4;
    private static final String ARG_FRAGMENT_TYPE = "param1";
    private int fragmentType;
    private SpendingsViewModel viewModel;

    private Drawable deleteicon;
    private ColorDrawable background;


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
            case FRAGMENT_TYPE_ALL_SPENDINGS:{

                deleteicon = ContextCompat.getDrawable(getContext(),
                        android.R.drawable.ic_menu_delete);
//                background = new ColorDrawable(getResources().getColor(R.color.BackgroundColorDark, getActivity().getTheme()));
                AllSpendingsAdapter allSpendingAdapter = new AllSpendingsAdapter(inflater);
                mRecyclerView.setAdapter(allSpendingAdapter);
                allSpendingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if(allSpendingAdapter.getItemCount() > 0){
                            if(txtEmpty.getVisibility() == View.VISIBLE){
                                txtEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });


                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Spendings spending  = allSpendingAdapter.getItem(position);
                        viewModel.purge();
                        viewModel.setCandelete(spending);
                        allSpendingAdapter.removeItem(position);
                        Snackbar.make(rootView, R.string.snack_txt_deleted, Snackbar.LENGTH_LONG)
                                .setAction(R.string.snack_txt_undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("spending fragment", "-----------------fragement ");
                                        viewModel.undoDelete(spending);
                                        allSpendingAdapter.undoRemove(position, spending);
                                    }
                                })
                                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        if(event == DISMISS_EVENT_TIMEOUT){
                                            Log.d("spending fragment", "-----------------dismis timeout fragement ");
                                            viewModel.delete(spending);
                                        }
                                    }
                                })
                                .show();

                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        View itemView = viewHolder.itemView;
                        int iconMargin = (itemView.getHeight() - deleteicon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - deleteicon.getIntrinsicHeight()) / 2;
                        int iconBottom = iconTop + deleteicon.getIntrinsicHeight();
                        int iconLeft = itemView.getRight() - iconMargin - deleteicon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        deleteicon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteicon.draw(c);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                });
                itemTouchHelper.attachToRecyclerView(mRecyclerView);

                LiveData<List<Spendings>> spendingsData = viewModel.getAllspendings();
                if(spendingsData.getValue() == null || spendingsData.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendingsData.observe(getViewLifecycleOwner(), spendings -> {
                    // Update the cached copy of the words in the adapter.
                    allSpendingAdapter.setSpendingsList(spendings);
                });

                allSpendingAdapter.setOnItemClickListener((view, position, spending) -> {

                    View dialogview = inflater.inflate(R.layout.selected_item, container);

                    MaterialTextView tv = dialogview.findViewById(R.id.dia_txt_amt);
                    tv.setText(String.valueOf(spending.amount));
                    tv = dialogview.findViewById(R.id.dia_txt_paidto);
                    tv.setText(spending.paidto);
                    tv = dialogview.findViewById(R.id.dia_txt_date);
                    tv.setText(DateUtils.sdtf.format(spending.whendt));
                    tv = dialogview.findViewById(R.id.dia_txt_remark);
                    tv.setText(spending.paidto);

                    new MaterialAlertDialogBuilder(getActivity())
                            .setView(dialogview)
                            .setPositiveButton("Edit", (d, w)->{
                                Intent intent = new Intent(getActivity(), RecordActivity.class);
                                intent.putExtra("Spending", spending);
                                intent.putExtra("ops", RecordActivity.OPS_UPDATE);
                                startActivity(intent);
                            })
                            .setNegativeButton("Cancel", (d, w)->{

                            })
                            .create()
                            .show();
                });

            }break;
            case FRAGMENT_TYPE_MONTHLY:{
                DMYSpendingsAdapter monthlySpendingsAdapter = new DMYSpendingsAdapter(DMYSpendingsAdapter.TYPE_MONTHLY,inflater);
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

                LiveData<List<DMYSpending>> spendingsData = viewModel.getMonthlySpendings();
                if(spendingsData.getValue() == null || spendingsData.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendingsData.observe(getViewLifecycleOwner(), spendings -> {
                    // Update the cached copy of the words in the adapter.
                    monthlySpendingsAdapter.setMonthlySpendingsList(spendings);
                });

            }break;
            case FRAGMENT_TYPE_YEARlY:{
                DMYSpendingsAdapter yearlySpendingsAdapter = new DMYSpendingsAdapter(DMYSpendingsAdapter.TYPE_YEARLY,inflater);
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

                LiveData<List<DMYSpending>> spendingsData = viewModel.getYearlySpendings();
                if(spendingsData.getValue() == null || spendingsData.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendingsData.observe(getViewLifecycleOwner(), spendings -> {
                    // Update the cached copy of the words in the adapter.
                    yearlySpendingsAdapter.setMonthlySpendingsList(spendings);
                });

            }break;

            case FRAGMENT_TYPE_DAILY:{
                DMYSpendingsAdapter dailySpendingsAdapter = new DMYSpendingsAdapter(DMYSpendingsAdapter.TYPE_DAILY,inflater);
                mRecyclerView.setAdapter(dailySpendingsAdapter);
                dailySpendingsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if(dailySpendingsAdapter.getItemCount() > 0){
                            if(txtEmpty.getVisibility() == View.VISIBLE){
                                txtEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });

                LiveData<List<DMYSpending>> spendingsData = viewModel.getDailySpendings();
                if(spendingsData.getValue() == null || spendingsData.getValue().isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }

                spendingsData.observe(getViewLifecycleOwner(), spendings -> {
                    // Update the cached copy of the words in the adapter.
                    dailySpendingsAdapter.setMonthlySpendingsList(spendings);
                });

            }break;
        }
        return rootView;
    }
}