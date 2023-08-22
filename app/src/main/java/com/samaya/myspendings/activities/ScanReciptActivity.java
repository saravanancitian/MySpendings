package com.samaya.myspendings.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.samaya.myspendings.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScanReciptActivity extends AppCompatActivity {


    LifecycleCameraController cameraController;
    PreviewView previewView;
    ActivityResultLauncher activityResultLauncher;
    String REQUIRED_PERMISSIONS[];
    TextRecognizer textRecognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_recipt);
        previewView = findViewById(R.id.preview);

        ArrayList<String> permissionslist = new ArrayList<>();
        permissionslist.add(Manifest.permission.CAMERA);
        permissionslist.add(Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            permissionslist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
        REQUIRED_PERMISSIONS = new String[permissionslist.size()];
        permissionslist.toArray(REQUIRED_PERMISSIONS);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                boolean permissonGranted = true;
                List<String> permissionlist = Arrays.asList(REQUIRED_PERMISSIONS);
                for(Map.Entry<String, Boolean> entry : result.entrySet()){
                    if (permissionlist.contains(entry.getKey()) && entry.getValue() == false) {
                        permissonGranted = false;
                        break;
                    }
                }
                if (!permissonGranted) {
                    Toast.makeText(getBaseContext(),
                            "Permission request denied",
                            Toast.LENGTH_SHORT).show();
                } else {
                    startCamera();
                }
            }
        });
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }

    void startCamera(){
        cameraController = new LifecycleCameraController(getBaseContext());
        cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);


        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        MlKitAnalyzer analyzer = new MlKitAnalyzer(Arrays.asList(textRecognizer), CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED, ContextCompat.getMainExecutor(this), new Consumer<MlKitAnalyzer.Result>() {
            @Override
            public void accept(MlKitAnalyzer.Result result) {
                Text visiontext = result.getValue(textRecognizer);
                if(visiontext != null) {

                    String visiontextstr = visiontext.getText();
                    if (visiontextstr.toUpperCase().contains("TOTAL")) {
                        Intent intent = new Intent();
                        intent.putExtra("scandata", visiontextstr);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    Log.d("Mlkit analyer", "visiontext is null");
                }
            }
        });


        cameraController.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(this),
                analyzer
        );

        previewView.setController(cameraController);
        cameraController.bindToLifecycle(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(textRecognizer != null) {
            textRecognizer.close();
        }

    }
}