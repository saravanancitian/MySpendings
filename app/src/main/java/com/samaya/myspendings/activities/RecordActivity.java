package com.samaya.myspendings.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.samaya.myspendings.R;
import com.samaya.myspendings.db.entity.Spendings;
import com.samaya.myspendings.fragments.SpendingsViewModel;
import com.samaya.myspendings.utils.DateUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class RecordActivity extends AppCompatActivity {

    private SpendingsViewModel viewModel;

    public static final String OPS_INSERT = "INSERT";
    public static final String OPS_UPDATE = "UPDATE";

    TextInputEditText editAmt;
    TextInputEditText editPaidto;
    TextInputEditText editWhendt;
    TextInputEditText editWhentime;

    TextInputEditText editRemark;

    String ops = OPS_INSERT;

    MaterialButton btnSave, btnCancel;

    MaterialDatePicker materialDatePicker;
    MaterialTimePicker materialTimePicker;


    Spendings spendingsforupdate;

    private AdView adView;

    ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_record);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        viewModel = (new ViewModelProvider(this).get(SpendingsViewModel.class));

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent resultData = result.getData();
                    String scandata = resultData.getStringExtra("scandata");
                    scandata = scandata.toUpperCase();
                    String scandatasplit[] = scandata.split("\n");
                    if(scandatasplit[0].contains("TOTAL")){

                    } else {
                        editPaidto.setText(scandatasplit[0]);
                        editRemark.setText(scandata);
                        for(String str: scandatasplit){
                            if(str.contains("TOTAL")){

                            }
                        }
                    }

                    Log.d("Record resute", scandata);

                }
            }
        });

        Intent receptintent = new Intent(RecordActivity.this, ScanReciptActivity.class);
        MaterialButton btnscanRecipt = findViewById(R.id.btnScanRecipt);
        btnscanRecipt.setOnClickListener(view -> {
            activityResultLauncher.launch(receptintent);
        });

        editAmt = findViewById(R.id.edit_amt);
        editPaidto = findViewById(R.id.edit_Paidto);
        editWhendt = findViewById(R.id.edit_whendt);
        editWhentime =  findViewById(R.id.edit_whentime);
        editRemark =  findViewById(R.id.edit_remark);

        editWhendt.setText(DateUtils.sdf.format(Calendar.getInstance().getTime()));
        editWhentime.setText(DateUtils.stf.format(Calendar.getInstance().getTime()));
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(view->finish());


        ops = intent.getStringExtra("ops");
        if(ops == null){
            ops = OPS_INSERT;
        } else{
            if(ops.equalsIgnoreCase(OPS_UPDATE)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    spendingsforupdate = intent.getParcelableExtra("Spending", Spendings.class);
                } else {
                    spendingsforupdate = intent.getParcelableExtra("Spending");
                }
                if(spendingsforupdate != null) {
                    editAmt.setText(String.valueOf(spendingsforupdate.amount));
                    editPaidto.setText(spendingsforupdate.paidto);
                    editRemark.setText(spendingsforupdate.remark);
                    editWhendt.setText(DateUtils.sdf.format(spendingsforupdate.whendt));
                    editWhentime.setText(DateUtils.stf.format(spendingsforupdate.whendt));
                }
            }
        }




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
            if(editAmt.getText() == null || TextUtils.isEmpty(editAmt.getText().toString()) || editPaidto.getText() == null || editPaidto.getText() == null || TextUtils.isEmpty(editPaidto.getText().toString()) ||
                    editWhendt.getText() == null || TextUtils.isEmpty(editWhendt.getText().toString()) || editWhentime.getText() == null ||TextUtils.isEmpty(editWhentime.getText().toString())
            || editRemark.getText() == null || TextUtils.isEmpty(editRemark.getText().toString())){
                Toast.makeText(getBaseContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Spendings spending = new Spendings(spendingsforupdate);
                    spending.amount = Float.parseFloat(editAmt.getText().toString());
                    spending.paidto = editPaidto.getText().toString();
                    spending.whendt = DateUtils.sdtf.parse(editWhendt.getText().toString() + " "+ editWhentime.getText().toString());
                    spending.remark = editRemark.getText().toString();
                   if(ops.equalsIgnoreCase("update")){
                        if(spendingsforupdate != null) {
                            viewModel.update(spending);
                        } else {
                            throw new Exception("Error with update object");
                        }
                    } else {
                        viewModel.insert(spending);
                    }

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
            String formattedDate  = DateUtils.sdf.format(calendar.getTime());
            editWhendt.setText(formattedDate);
        });

        materialTimePicker = (new MaterialTimePicker.Builder()).setTitleText("Select a time").build();
        materialTimePicker.addOnPositiveButtonClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
            cal.set(Calendar.MINUTE, materialTimePicker.getMinute());
             editWhentime.setText(DateUtils.stf.format(cal.getTime()));
        });
        editWhendt.setOnClickListener(view -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        editWhentime.setOnClickListener(view -> materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER"));



    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}