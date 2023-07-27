package com.samaya.myspendings;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.samaya.myspendings.db.entity.MonthlyOrYearlySpending;
import com.samaya.myspendings.db.entity.Spendings;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class RecordActivity extends AppCompatActivity {

    private SpendingsViewModel viewModel;

    TextInputEditText editAmt;
    TextInputEditText editPaidto;
    TextInputEditText editWhendt;
    TextInputEditText editWhentime;

    TextInputEditText editRemark;

    String ops = "insert";

    MaterialButton btnSave;

    MaterialDatePicker materialDatePicker;
    MaterialTimePicker materialTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_record);
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));



        editAmt = findViewById(R.id.edit_amt);
        editPaidto = findViewById(R.id.edit_Paidto);
        editWhendt = findViewById(R.id.edit_whendt);
        editWhentime =  findViewById(R.id.edit_whentime);
        editRemark =  findViewById(R.id.edit_remark);

        editWhendt.setText(Utils.sdf.format(Calendar.getInstance().getTime()));
        editWhentime.setText(Utils.stf.format(Calendar.getInstance().getTime()));
        btnSave = findViewById(R.id.btn_save);







        editPaidto.addTextChangedListener(new TextWatcher() {

            boolean canEdit;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(editRemark.getText())){
                    canEdit = true;
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(canEdit) {
                    editRemark.setText(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        btnSave.setOnClickListener(view -> {
            Spendings spending = new Spendings();
            if(editAmt.getText() == null || TextUtils.isEmpty(editAmt.getText().toString()) || editPaidto.getText() == null || editPaidto.getText() == null || TextUtils.isEmpty(editPaidto.getText().toString()) ||
                    editWhendt.getText() == null || TextUtils.isEmpty(editWhendt.getText().toString()) || editWhentime.getText() == null ||TextUtils.isEmpty(editWhentime.getText().toString())
            || editRemark.getText() == null || TextUtils.isEmpty(editRemark.getText().toString())){
                Toast.makeText(getBaseContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    spending.amount = Float.parseFloat(editAmt.getText().toString());
                    spending.paidto = editPaidto.getText().toString();
                    spending.whendt = Utils.sdtf.parse(editWhendt.getText().toString() + " "+ editWhentime.getText().toString());
                    spending.remark = editRemark.getText().toString();
                    viewModel.insert(spending);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    finish();
                }
            }
        });
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDatePicker = materialDateBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            String formattedDate  = Utils.sdf.format(calendar.getTime());
            editWhendt.setText(formattedDate);
        });

        materialTimePicker = (new MaterialTimePicker.Builder()).setTitleText("Select a time").build();
        materialTimePicker.addOnPositiveButtonClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
            cal.set(Calendar.MINUTE, materialTimePicker.getMinute());
             editWhentime.setText(Utils.stf.format(cal.getTime()));
        });
        editWhendt.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        editWhentime.setOnClickListener(view -> materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER"));

        ops = intent.getStringExtra("ops");
        if(ops != null){
            if(ops.equalsIgnoreCase("update")){
                Float amount =  intent.getFloatExtra("amount", 0);
                String paidto = intent.getStringExtra("paidto");
                String remark = intent.getStringExtra("remark");
                String whendate = intent.getStringExtra("whendate");
                String whentime = intent.getStringExtra("whentime");
                editAmt.setText(String.valueOf(amount));
                editPaidto.setText(paidto);
                editRemark.setText(remark);
                editWhendt.setText(whendate);
                editWhentime.setText(whentime);
            }
        }

    }
}