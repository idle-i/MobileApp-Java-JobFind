package com.example.jobfind;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonApi {
    @GET("positions.json/?page=1")
    Call<List<JobModel>> getJobList(@Query("search") String search, @Query("type") String emp);
}
