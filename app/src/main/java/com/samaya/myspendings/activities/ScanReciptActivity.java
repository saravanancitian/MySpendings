package com.samaya.myspendings.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import androidx.core.util.Consumer;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognition;

import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.samaya.myspendings.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ScanReciptActivity extends AppCompatActivity {


    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    ActivityResultLauncher activityResultLauncher;
    String REQUIRED_PERMISSIONS[];
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
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        MlKitAnalyzer analyzer = new MlKitAnalyzer(Arrays.asList(textRecognizer), ImageAnalysis.COORDINATE_SYSTEM_ORIGINAL, ContextCompat.getMainExecutor(this), new Consumer<MlKitAnalyzer.Result>() {
            @Override
            public void accept(MlKitAnalyzer.Result result) {
                Text visiontext = result.getValue(textRecognizer);
                String visiontextstr = visiontext.getText();
                if(visiontextstr.toUpperCase().contains("TOTAL")){
                    Intent intent = new Intent();
                    intent.putExtra("scandata", visiontextstr);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

       ResolutionSelector selector =  (new ResolutionSelector.Builder())
               .setResolutionStrategy(new ResolutionStrategy(new Size(1280, 720), ResolutionStrategy.FALLBACK_RULE_NONE))
               .build();

        ImageAnalysis imageAnalysis =  new ImageAnalysis.Builder()
                // enable the following line if RGBA output is needed.
                //.setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setResolutionSelector(selector)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer( ContextCompat.getMainExecutor(this), analyzer);

        cameraProvider.unbindAll();
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector,  imageAnalysis, preview);
    }
}