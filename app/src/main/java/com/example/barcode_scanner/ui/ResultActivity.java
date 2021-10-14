package com.example.barcode_scanner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barcode_scanner.R;
import com.example.barcode_scanner.data.CheckQrCodeResponse;
import com.example.barcode_scanner.data.Constants;
import com.example.barcode_scanner.databinding.ActivityResultBinding;
import com.example.barcode_scanner.databinding.QrCheckerDialogBinding;
import com.example.barcode_scanner.network.CodeCheckerService;
import com.example.barcode_scanner.network.ServiceGenerator;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();
    private QrCheckerDialogBinding qrCheckerDialogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResultBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_result);

        binding.btnScanCode.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
            startActivityForResult(intent, Constants.SCAN_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "request code : " + requestCode);

        Log.d(TAG, "result code : " + resultCode);

        if (requestCode == Constants.SCAN_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                if (data != null) {
                    String scannedCode = data.getStringExtra(Constants.SCANNED_RESULT_DATA_KEY);

                    showQRCodeCheckerDialog(this);

                    checkCodeWithServer(scannedCode);
                }

            }

        }
    }

    private void checkCodeWithServer(String scannedCode) {
        CodeCheckerService service = ServiceGenerator.createService(CodeCheckerService.class);

        service.checkQrCodeInServer(scannedCode).enqueue(new Callback<CheckQrCodeResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckQrCodeResponse> call, @NonNull Response<CheckQrCodeResponse> response) {
                Log.d(TAG,"response code : " + response.code());
                Log.d(TAG,"response body : " + response.body());

                if (response.code() == 200 && response.body() != null) {
                    qrCheckerDialogBinding.successView.setVisibility(View.VISIBLE);
                    qrCheckerDialogBinding.loadingView.setVisibility(View.GONE);
                    qrCheckerDialogBinding.successMessage.setText(response.body().getMessage());
                    return;
                }

                qrCheckerDialogBinding.loadingView.setVisibility(View.GONE);
                qrCheckerDialogBinding.errorView.setVisibility(View.VISIBLE);
                qrCheckerDialogBinding.errorMessage.setText(response.body().getMessage());
            }

            @Override
            public void onFailure(@NonNull Call<CheckQrCodeResponse> call,  @NonNull Throwable t) {
                Log.d(TAG,"error message : " + t.getLocalizedMessage());

                qrCheckerDialogBinding.errorView.setVisibility(View.VISIBLE);
                qrCheckerDialogBinding.loadingView.setVisibility(View.GONE);
                qrCheckerDialogBinding.errorMessage.setText(t.getLocalizedMessage());
            }
        });
    }

    private void showQRCodeCheckerDialog(Context context) {
        qrCheckerDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.qr_checker_dialog,
                null,
                false
        );

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setView(qrCheckerDialogBinding.getRoot())
                .setCancelable(true)
                .create();

        dialog.show();

        qrCheckerDialogBinding.cancel.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
            startActivityForResult(intent, Constants.SCAN_REQUEST_CODE);
        });

        qrCheckerDialogBinding.finish.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
            startActivityForResult(intent, Constants.SCAN_REQUEST_CODE);
        });

        qrCheckerDialogBinding.close.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
            startActivityForResult(intent, Constants.SCAN_REQUEST_CODE);
        });
    }
}