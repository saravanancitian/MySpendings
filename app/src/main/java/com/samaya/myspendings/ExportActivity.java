package com.samaya.myspendings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
    }

    void saveFile(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "example.txt");

//Write to file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Writing to file!");
        } catch (IOException e) {
            //Handle exception
        }
    }
}