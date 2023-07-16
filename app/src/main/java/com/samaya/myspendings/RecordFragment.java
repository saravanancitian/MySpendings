package com.samaya.myspendings;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final Calendar newCalendar = Calendar.getInstance();
    private SpendingsViewModel viewModel;

    EditText editAmt;
    EditText editPaidto;
    EditText editWhendt;
    EditText editWhentime;

    Button btnSave;

    MaterialDatePicker materialDatePicker;
    MaterialTimePicker materialTimePicker;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
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
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        editAmt = (EditText)  view.findViewById(R.id.edit_amt);
        editPaidto = (EditText) view.findViewById(R.id.edit_Paidto);

        editWhendt = (EditText) view.findViewById(R.id.edit_whendt);
        editWhendt.setText(Utils.sdf.format(Calendar.getInstance().getTime()));
        editWhentime = (EditText) view.findViewById(R.id.edit_whentime);
        editWhentime.setText(Utils.stf.format(Calendar.getInstance().getTime()));
        btnSave = (Button) view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Spendings spending = new Spendings();
                spending.amount = Float.parseFloat(editAmt.getText().toString());
                spending.paidto = editPaidto.getText().toString();
                try {
                    spending.whenDt = (Date) Utils.sdf.parse(editWhendt.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                viewModel.insert(spending);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, SpendingsFragment.class, null)
                        .commit();
            }
        });
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(selection);
                String formattedDate  = Utils.sdf.format(calendar.getTime());
                editWhendt.setText(formattedDate);
            }
        });

        materialTimePicker = (new MaterialTimePicker.Builder()).setTitleText("Select a time").build();
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 editWhentime.setText(Utils.stf.format(materialTimePicker.getHour() * 60 +  materialTimePicker.getMinute()) );
            }
        });
        editWhendt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });

        editWhentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialTimePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });
        return view;
    }


}