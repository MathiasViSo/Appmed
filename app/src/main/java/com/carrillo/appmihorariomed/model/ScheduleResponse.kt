package com.carrillo.appmihorariomed.model

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    @SerializedName("content")
    val content: List<Schedule>
)