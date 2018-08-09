package com.erp.sheelafoam.utils;


import com.erp.sheelafoam.models.ImageResponseBody;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by admin on 21-12-2017.
 */

public interface APIView {

    @POST(URLS.UPLOAD_PHOTO)
    @Multipart
    Call<ImageResponseBody> uploadImage(@Path("uid") String uId, @Path("token") String token ,@Path("storeId") String storeId, @Part MultipartBody.Part image, @Part("employeeId") RequestBody employeeId);


}
