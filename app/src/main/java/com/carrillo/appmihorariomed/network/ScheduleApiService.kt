package com.carrillo.appmihorariomed.network

import com.carrillo.appmihorariomed.model.Schedule
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ScheduleApiService {
    @GET("schedules")
    suspend fun getSchedules(): Response<List<Schedule>>

    @POST("schedules")
    suspend fun createSchedule(@Body schedule: Schedule): Response<Schedule>

    @PUT("schedules/{id}")
    suspend fun updateSchedule(
        @Path("id") id: Int,
        @Body schedule: Schedule
    ): Response<Schedule>

    @DELETE("schedules/{id}")
    suspend fun deleteSchedule(@Path("id") id: Int): Response<Unit>
}
