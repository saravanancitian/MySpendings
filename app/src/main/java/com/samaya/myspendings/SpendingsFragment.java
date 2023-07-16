package com.samaya.myspendings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpendingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpendingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected RecyclerView mRecyclerView;
    protected TextView mEmptyView;
    protected SpendingsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private SpendingsViewModel viewModel;


    public SpendingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpendingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpendingsFragment newInstance(String param1, String param2) {
        SpendingsFragment fragment = new SpendingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_spendings, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mAdapter = new SpendingsAdapter(inflater);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mEmptyView = (TextView) rootView.findViewById(R.id.txt_empty);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, RecordFragment.class, null)
                        .commit();
            }
        });

        LiveData<List<Spendings>> allSpending =  viewModel.getAllspendings();
        if(allSpending == null || allSpending.getValue() == null || allSpending.getValue().isEmpty()){
            mEmptyView.setVisibility(View.VISIBLE);
        }else{
            mEmptyView.setVisibility(View.GONE);
        }

        allSpending.observe(getViewLifecycleOwner(), new Observer<List<Spendings>>() {
            @Override
            public void onChanged(@Nullable final List<Spendings> spendings) {
                // Update the cached copy of the words in the adapter.
                if(mEmptyView.getVisibility() == View.VISIBLE) {
                    mEmptyView.setVisibility(View.GONE);
                }
                mAdapter.setSpendingsList(spendings);
            }
        });
        return rootView;
    }
}