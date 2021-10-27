package com.example.mybracketapp.model

import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication

class MatchCompetitor(val competitor: Competitor, val beltColor: Color) {

    constructor() : this(Competitor(), Color.None)

    enum class Color {
        Red,
        Blue,
        None
    }

    var winner: Boolean = false
    var matchResult: String = ""
    var place:Int = -1

    fun getPlaceColor(): Int {
        return when(place){
            1 -> SLApplication.getContext().getColor(R.color.gold)
            2 -> SLApplication.getContext().getColor(R.color.silver)
            3 -> SLApplication.getContext().getColor(R.color.bronze)
            else -> 0
        }
    }

    fun getBeltColorResource(): Int {
        return when(beltColor){
            Color.Red -> SLApplication.getContext().getColor(R.color.sambo_red)
            Color.Blue -> SLApplication.getContext().getColor(R.color.sambo_blue)
            Color.None -> SLApplication.getContext().getColor(R.color.white)
        }
    }

    fun getBeltColor25Resource(): Int {
        return when(beltColor){
            Color.Red -> SLApplication.getContext().getColor(R.color.sambo_red_25)
            Color.Blue -> SLApplication.getContext().getColor(R.color.sambo_blue_25)
            Color.None -> SLApplication.getContext().getColor(R.color.white)
        }
    }

    fun isPlaceholder(): Boolean {
        return competitor.isPlaceholder()
    }


}