package com.malikrafsan.restaurant_mobile_app.api

import com.malikrafsan.restaurant_mobile_app.dto.Branch
import retrofit2.Call
import retrofit2.http.GET

interface BranchApi {
    @GET("branch")
    fun getBranch(): Call<Branch>
}