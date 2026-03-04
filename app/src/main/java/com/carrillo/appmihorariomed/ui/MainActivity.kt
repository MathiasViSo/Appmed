package com.carrillo.appmihorariomed.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.carrillo.appmihorariomed.network.RetrofitClient
import com.carrillo.appmihorariomed.repository.ScheduleRepository
import com.carrillo.appmihorariomed.viewmodel.ScheduleViewModel
import com.carrillo.appmihorariomed.viewmodel.ScheduleViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: ScheduleViewModel by viewModels {
        ScheduleViewModelFactory(
            ScheduleRepository(RetrofitClient.apiService)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ScheduleScreen(viewModel = viewModel)
            }
        }
    }
}