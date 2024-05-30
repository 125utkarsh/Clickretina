

package com.example.clickretina;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("v3/f4613593-a726-4908-84cf-08b5b96c4a57")
    Call<ApiResponse> getData();
}

