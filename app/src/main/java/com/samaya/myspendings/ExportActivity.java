package com.samaya.myspendings;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.samaya.myspendings.db.entity.Spendings;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    private ReportViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);



        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.CreateDocument("text/csv"),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Log.d("Activity Request ", " ----- path : "+result.getPath()+" "+result.toString());

                    }
                }
        );
        MaterialButton btn_export_all = findViewById(R.id.btn_export_all);

        btn_export_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                launcher.launch("test.txt");
            }
        });

        MaterialButton btn_export_range = findViewById(R.id.btn_export_range);
        btn_export_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

    void saveFile(Uri fileuri, String data){
        OutputStream os = null;

        try {
            os = getContentResolver().openOutputStream(fileuri);
            os.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(os != null)
                    os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}