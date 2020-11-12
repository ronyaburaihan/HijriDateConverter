package com.techdoctorbd.arabicdateconverter

import com.google.gson.annotations.SerializedName

data class HijriMonth(
    @SerializedName("start")
    val startDate: String,
    @SerializedName("end")
    val endDate: String,
    @SerializedName("name")
    val monthName: String,
    @SerializedName("hijri_year")
    val hijriYear: String
)