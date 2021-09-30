package com.example.barcode_scanner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.barcode_scanner.R;
import com.example.barcode_scanner.data.CheckQrCodeResponse;
import com.example.barcode_scanner.data.Constants;
import com.example.barcode_scanner.data.QrCodeSchema;
import com.example.barcode_scanner.databinding.ActivityResultBinding;
import com.example.barcode_scanner.databinding.QrCheckerDialogBinding;
import com.example.barcode_scanner.network.CodeCheckerService;
import com.example.barcode_scanner.network.ServiceGenerator;

import org.jetbrains.annotations.NotNull;

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
        CodeCheckerService service = ServiceGenerator.getService(CodeCheckerService.class);

        service.checkQrCodeInServer(new QrCodeSchema(scannedCode)).enqueue(new Callback<Response<CheckQrCodeResponse>>() {
            @Override
            public void onResponse(@NotNull Call<Response<CheckQrCodeResponse>> call, @NotNull Response<Response<CheckQrCodeResponse>> response) {

            }

            @Override
            public void onFailure(@NotNull Call<Response<CheckQrCodeResponse>> call, @NotNull Throwable t) {

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