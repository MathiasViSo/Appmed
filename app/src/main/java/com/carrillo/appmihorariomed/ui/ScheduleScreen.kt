package com.carrillo.appmihorariomed.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.carrillo.appmihorariomed.model.Schedule
import com.carrillo.appmihorariomed.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.errorMessage, viewModel.successMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
        viewModel.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Horarios medicos") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.dosis,
                onValueChange = viewModel::onDosisChange,
                label = { Text("Dosis") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.hora,
                onValueChange = viewModel::onHoraChange,
                label = { Text("Hora (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.frecuencia,
                onValueChange = viewModel::onFrecuenciaChange,
                label = { Text("Frecuencia") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.notas,
                onValueChange = viewModel::onNotasChange,
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Activo", modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.activo,
                    onCheckedChange = viewModel::onActivoChange
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::saveOrUpdate
                ) {
                    Text(if (viewModel.editingId == null) "Agregar" else "Actualizar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::clearForm
                ) {
                    Text("Limpiar")
                }
            }

            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text("Buscar por nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.filteredSchedules, key = { it.id }) { schedule ->
                    ScheduleItem(
                        schedule = schedule,
                        onEdit = { viewModel.startEdit(schedule) },
                        onDelete = { viewModel.deleteSchedule(schedule.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleItem(
    schedule: Schedule,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = schedule.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Dosis: ${schedule.dosis}")
                Text(text = "Hora: ${schedule.hora}")
                Text(text = "Frecuencia: ${schedule.frecuencia}")
                if (!schedule.notas.isNullOrBlank()) {
                    Text(text = "Notas: ${schedule.notas}")
                }
                Text(text = if (schedule.activo) "Estado: Activo" else "Estado: Inactivo")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar"
                )
            }
        }
    }
}
