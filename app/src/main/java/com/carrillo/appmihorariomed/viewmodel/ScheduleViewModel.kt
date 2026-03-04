package com.carrillo.appmihorariomed.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.carrillo.appmihorariomed.model.Schedule
import com.carrillo.appmihorariomed.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {

    var allSchedules by mutableStateOf<List<Schedule>>(emptyList())
        private set

    var filteredSchedules by mutableStateOf<List<Schedule>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var nombre by mutableStateOf("")
        private set
    var dosis by mutableStateOf("")
        private set
    var hora by mutableStateOf("")
        private set
    var frecuencia by mutableStateOf("")
        private set
    var notas by mutableStateOf("")
        private set
    var activo by mutableStateOf(true)
        private set

    var editingId by mutableStateOf<Int?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadSchedules()
    }

    fun onSearchQueryChange(value: String) {
        searchQuery = value
        applyFilter()
    }

    fun onNombreChange(value: String) { nombre = value }
    fun onDosisChange(value: String) { dosis = value }
    fun onHoraChange(value: String) { hora = value }
    fun onFrecuenciaChange(value: String) { frecuencia = value }
    fun onNotasChange(value: String) { notas = value }
    fun onActivoChange(value: Boolean) { activo = value }

    fun startEdit(schedule: Schedule) {
        editingId = schedule.id
        nombre = schedule.nombre
        dosis = schedule.dosis
        hora = schedule.hora
        frecuencia = schedule.frecuencia
        notas = schedule.notas.orEmpty()
        activo = schedule.activo
    }

    fun clearForm() {
        editingId = null
        nombre = ""
        dosis = ""
        hora = ""
        frecuencia = ""
        notas = ""
        activo = true
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }

    fun loadSchedules() {
        viewModelScope.launch {
            isLoading = true
            clearMessages()
            repository.getSchedules()
                .onSuccess {
                    allSchedules = it
                    applyFilter()
                }
                .onFailure {
                    errorMessage = "No se pudo cargar: ${it.message}"
                }
            isLoading = false
        }
    }

    fun saveOrUpdate() {
        if (nombre.isBlank() || dosis.isBlank() || hora.isBlank() || frecuencia.isBlank()) {
            errorMessage = "Completa los campos obligatorios"
            return
        }

        val schedule = Schedule(
            id = editingId ?: 0,
            nombre = nombre.trim(),
            dosis = dosis.trim(),
            hora = hora.trim(),
            frecuencia = frecuencia.trim(),
            notas = notas.trim().ifBlank { null },
            activo = activo
        )

        viewModelScope.launch {
            isLoading = true
            clearMessages()

            val result = if (editingId == null) {
                repository.createSchedule(schedule)
            } else {
                repository.updateSchedule(schedule)
            }

            result.onSuccess {
                successMessage = if (editingId == null) {
                    "Horario creado"
                } else {
                    "Horario actualizado"
                }
                clearForm()
                loadSchedules()
            }.onFailure {
                errorMessage = "Operacion fallida: ${it.message}"
            }

            isLoading = false
        }
    }

    fun deleteSchedule(id: Int) {
        viewModelScope.launch {
            isLoading = true
            clearMessages()
            repository.deleteSchedule(id)
                .onSuccess {
                    successMessage = "Horario eliminado"
                    if (editingId == id) {
                        clearForm()
                    }
                    loadSchedules()
                }
                .onFailure {
                    errorMessage = "No se pudo eliminar: ${it.message}"
                }
            isLoading = false
        }
    }

    private fun applyFilter() {
        val query = searchQuery.trim().lowercase()
        filteredSchedules = if (query.isBlank()) {
            allSchedules
        } else {
            allSchedules.filter { it.nombre.lowercase().contains(query) }
        }
    }
}

class ScheduleViewModelFactory(
    private val repository: ScheduleRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
