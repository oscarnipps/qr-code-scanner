package com.example.barcode_scanner.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.barcode_scanner.data.Constants;
import com.example.barcode_scanner.R;
import com.example.barcode_scanner.databinding.ActivityMainBinding;
import com.google.zxing.Result;

public class ScanActivity extends AppCompatActivity {
    public static final String TAG = ScanActivity.class.getSimpleName();
    private CodeScanner mCodeScanner;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpPermission();

        setUpCodeScanner();
    }

    private void setUpCodeScanner() {
        mCodeScanner = new CodeScanner(this, binding.scannerView);

        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);

        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);

        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);

        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String scannedResult = result.getText();

                        //Toast.makeText(ScanActivity.this, "scan success", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "scanned result : " + scannedResult);

                        Intent intent = new Intent();

                        intent.putExtra(Constants.SCANNED_RESULT_DATA_KEY,scannedResult);

                        setResult(RESULT_OK,intent);

                        finish();
                    }
                });
            }
        });

        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull final Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanActivity.this, "scan failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "code scan failed with error : " + error.getLocalizedMessage());
                    }
                });
            }
        });
    }

    private void setUpPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this ,
                    new String[]{Manifest.permission.CAMERA},
                    Constants.CAMERA_REQUEST_CODE
            );

            return;
        }

        binding.scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(ScanActivity.this, "scan failed due to low memory", Toast.LENGTH_SHORT).show();
    }
}