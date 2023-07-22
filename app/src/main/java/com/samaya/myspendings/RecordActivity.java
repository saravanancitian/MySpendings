package com.samaya.myspendings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.samaya.myspendings.db.entity.Spendings;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class RecordActivity extends AppCompatActivity {

    final Calendar newCalendar = Calendar.getInstance();
    private SpendingsViewModel viewModel;

    EditText editAmt;
    EditText editPaidto;
    EditText editWhendt;
    EditText editWhentime;

    Button btnSave;

    MaterialDatePicker materialDatePicker;
    MaterialTimePicker materialTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));
        editAmt = (EditText)  findViewById(R.id.edit_amt);
        editPaidto = (EditText) findViewById(R.id.edit_Paidto);

        editWhendt = (EditText) findViewById(R.id.edit_whendt);
        editWhendt.setText(Utils.sdf.format(Calendar.getInstance().getTime()));
        editWhentime = (EditText) findViewById(R.id.edit_whentime);
        editWhentime.setText(Utils.stf.format(Calendar.getInstance().getTime()));
        btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Spendings spending = new Spendings();
                if(TextUtils.isEmpty(editAmt.getText().toString()) || TextUtils.isEmpty(editPaidto.getText().toString()) ||
                        TextUtils.isEmpty(editWhendt.getText().toString()) || TextUtils.isEmpty(editWhentime.getText().toString())){
                    Toast.makeText(getBaseContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        spending.amount = Float.parseFloat(editAmt.getText().toString());
                        spending.paidto = editPaidto.getText().toString();
                        spending.whenDt = (Date) Utils.sdtf.parse(editWhendt.getText().toString() + " "+ editWhentime.getText().toString());
                        viewModel.insert(spending);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        finish();
                    }
                }
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
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                cal.set(Calendar.MINUTE, materialTimePicker.getMinute());
                 editWhentime.setText(Utils.stf.format(cal.getTime()));
            }
        });
        editWhendt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            }
        });

        editWhentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });

    }
}