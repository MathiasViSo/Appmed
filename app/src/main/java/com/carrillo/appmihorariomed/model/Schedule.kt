package com.carrillo.appmihorariomed.model

import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("dosis")
    val dosis: String,
    @SerializedName("hora")
    val hora: String,
    @SerializedName("frecuencia")
    val frecuencia: String,
    @SerializedName("notas")
    val notas: String? = null,
    @SerializedName("activo")
    val activo: Boolean = true
)
