package com.carrillo.appmihorariomed.repository

import com.carrillo.appmihorariomed.model.Schedule
import com.carrillo.appmihorariomed.model.ScheduleResponse // <-- IMPORTANTE
import com.carrillo.appmihorariomed.network.ScheduleApiService

class ScheduleRepository(private val api: ScheduleApiService) {

    suspend fun getSchedules(): Result<List<Schedule>> {
        return runCatching {
            val response = api.getSchedules()
            if (!response.isSuccessful) throw Exception("Error ${response.code()}")

            // Extraemos la lista que viene dentro de 'content'
            val body = response.body()
            body?.content ?: emptyList()
        }
    }

    suspend fun createSchedule(schedule: Schedule): Result<Schedule> {
        return runCatching {
            val scheduleToCreate = schedule.copy(id = null)
            val response = api.createSchedule(scheduleToCreate)
            if (!response.isSuccessful) throw Exception("Error ${response.code()}")
            response.body() ?: throw Exception("Respuesta vacía")
        }
    }

    suspend fun updateSchedule(schedule: Schedule): Result<Schedule> {
        return runCatching {
            val response = api.updateSchedule(schedule.id!!, schedule)
            if (!response.isSuccessful) throw Exception("Error ${response.code()}")
            response.body() ?: throw Exception("Respuesta vacía")
        }
    }

    suspend fun deleteSchedule(id: Int): Result<Unit> {
        return runCatching {
            val response = api.deleteSchedule(id)
            if (!response.isSuccessful) throw Exception("Error ${response.code()}")
            Unit
        }
    }
}