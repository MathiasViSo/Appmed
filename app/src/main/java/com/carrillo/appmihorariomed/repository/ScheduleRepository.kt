package com.carrillo.appmihorariomed.repository

import com.carrillo.appmihorariomed.model.Schedule
import com.carrillo.appmihorariomed.network.ScheduleApiService

class ScheduleRepository(private val api: ScheduleApiService) {

    suspend fun getSchedules(): Result<List<Schedule>> {
        return runCatching {
            val response = api.getSchedules()
            if (!response.isSuccessful) error("Error ${response.code()}")
            response.body().orEmpty()
        }
    }

    suspend fun createSchedule(schedule: Schedule): Result<Schedule> {
        return runCatching {
            val response = api.createSchedule(schedule)
            if (!response.isSuccessful) error("Error ${response.code()}")
            response.body() ?: error("Respuesta vacia")
        }
    }

    suspend fun updateSchedule(schedule: Schedule): Result<Schedule> {
        return runCatching {
            val response = api.updateSchedule(schedule.id, schedule)
            if (!response.isSuccessful) error("Error ${response.code()}")
            response.body() ?: error("Respuesta vacia")
        }
    }

    suspend fun deleteSchedule(id: Int): Result<Unit> {
        return runCatching {
            val response = api.deleteSchedule(id)
            if (!response.isSuccessful) error("Error ${response.code()}")
            Unit
        }
    }
}
