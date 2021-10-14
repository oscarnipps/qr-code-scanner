package com.example.barcode_scanner.network;

import com.example.barcode_scanner.data.CheckQrCodeResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CodeCheckerService {

    @POST("validateqrcodes/check/{qrCode}")
    Call<CheckQrCodeResponse> checkQrCodeInServer(@Path( "qrCode") String qrCode);
}
