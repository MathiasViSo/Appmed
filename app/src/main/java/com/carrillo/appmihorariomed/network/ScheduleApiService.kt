package com.carrillo.appmihorariomed.network

import com.carrillo.appmihorariomed.model.Schedule
import com.carrillo.appmihorariomed.model.ScheduleResponse // <-- IMPORTANTE
import retrofit2.Response
import retrofit2.http.*

interface ScheduleApiService {
    @GET("api/horariomed")
    suspend fun getSchedules(): Response<ScheduleResponse> // Ahora usa ScheduleResponse

    @POST("api/horariomed")
    suspend fun createSchedule(@Body schedule: Schedule): Response<Schedule>

    @PUT("api/horariomed/{id}")
    suspend fun updateSchedule(@Path("id") id: Int, @Body schedule: Schedule): Response<Schedule>

    @DELETE("api/horariomed/{id}")
    suspend fun deleteSchedule(@Path("id") id: Int): Response<Unit>
}