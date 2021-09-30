package com.example.barcode_scanner.network;

import com.example.barcode_scanner.data.CheckQrCodeResponse;
import com.example.barcode_scanner.data.Constants;
import com.example.barcode_scanner.data.QrCodeSchema;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CodeCheckerService {

    @POST(Constants.ENDPOINT_CHECK_QR_CODE)
    Call<Response<CheckQrCodeResponse>> checkQrCodeInServer(@Body QrCodeSchema qrCode);
}
