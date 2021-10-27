package com.example.mybracketapp.model

import com.google.gson.annotations.SerializedName

data class CompetitorsJson(@SerializedName("version") val version: Double,
                           @SerializedName("competitors") val competitors: ArrayList<Competitor>) {
}