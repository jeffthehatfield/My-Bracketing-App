package com.example.mybracketapp.model

import android.util.Log
import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication

class MatchData(val index: Int, private val callback: MatchCallback, val winnerNextIndex: Int, val loserNextIndex: Int,
                competitorTop: Competitor, competitorBottom: Competitor) {

    constructor(index: Int, callback: MatchCallback, winnerNextIndex: Int, loserNextIndex: Int)
            :this(index, callback, winnerNextIndex, loserNextIndex, Competitor(), Competitor())

    companion object {
        const val FIRST_PLACE = -1
        const val SECOND_PLACE = -2
        const val THIRD_PLACE = -3
        const val EMPTY = -99
        const val WIN_COND_POINTS = "Points"
        const val WIN_COND_SUBMISSION = "Submission"
        const val WIN_COND_TIME = "Time"
        const val WIN_COND_DQ = "DQ"
        const val WIN_COND_TKO = "TKO"
        const val WIN_COND_KO = "KO"

        @JvmStatic
        val WIN_COND_ARRAY = arrayOf(WIN_COND_POINTS, WIN_COND_SUBMISSION, WIN_COND_TIME, WIN_COND_DQ, WIN_COND_TKO, WIN_COND_KO)
    }

    private var winCondition: String = ""
    private var submission: String = ""
    private var matchCompetitorTop: MatchCompetitor =
        if(competitorTop.isPlaceholder()) MatchCompetitor()
        else MatchCompetitor(competitorTop, MatchCompetitor.Color.Red)
    private var matchCompetitorBottom: MatchCompetitor =
        if(competitorBottom.isPlaceholder()) MatchCompetitor()
        else MatchCompetitor(competitorBottom, MatchCompetitor.Color.Blue)

    fun setPointResult(competitorTopPoints: Int, competitorBottomPoints: Int) {
        winCondition = WIN_COND_POINTS
        setPoints(competitorTopPoints, competitorBottomPoints)
    }

    fun setTimeResult(competitorTopPoints: Int, competitorBottomPoints: Int) {
        winCondition = WIN_COND_TIME
        setPoints(competitorTopPoints, competitorBottomPoints)
    }

    private fun setPoints(competitorTopPoints: Int, competitorBottomPoints: Int) {
        if(competitorTopPoints > competitorBottomPoints){
            matchCompetitorTop.winner = true
        }
        else{
            matchCompetitorBottom.winner = true
        }
        getWinner()?.matchResult = SLApplication.getContext().getString(
            if(winCondition == WIN_COND_POINTS) R.string.win_condition_points_label else R.string.win_condition_time_label,
            if(didTopWin()) competitorTopPoints else competitorBottomPoints)
        getLoser()?.matchResult = if(didTopWin()) competitorBottomPoints.toString() else competitorTopPoints.toString()
        setResults()
    }

    fun setSubmissionResult(sub: String, competitor: MatchCompetitor) {
        winCondition = WIN_COND_SUBMISSION
        submission = sub
        setWinner(competitor)
        getWinner()?.matchResult = SLApplication.getContext().getString(R.string.win_condition_submission_label, sub)
        setResults()
    }

    fun setDQResult(competitor: MatchCompetitor){
        winCondition = WIN_COND_DQ
        setWinner(competitor)
        getLoser()?.matchResult = SLApplication.getContext().getString(R.string.loser_condition_dq_label)
        setResults()
    }

    fun setTKOResult(competitor: MatchCompetitor){
        winCondition = WIN_COND_TKO
        setWinner(competitor)
        getWinner()?.matchResult = SLApplication.getContext().getString(R.string.win_condition_tko_label)
        setResults()
    }

    fun setKOResult(competitor: MatchCompetitor){
        winCondition = WIN_COND_KO
        setWinner(competitor)
        getWinner()?.matchResult = SLApplication.getContext().getString(R.string.win_condition_ko_label)
        setResults()
    }

    private fun setWinner(competitor: MatchCompetitor) {
        if(competitor == matchCompetitorTop) {
            matchCompetitorTop.winner = true
        } else{
            matchCompetitorBottom.winner = true
        }
    }

    private fun setResults() {
        when(winnerNextIndex) {
            FIRST_PLACE -> {
                if(didTopWin()) {
                    matchCompetitorTop.place = 1
                }else {
                    matchCompetitorBottom.place = 1
                }
            }
            SECOND_PLACE -> {
                if(didTopWin()) {
                    matchCompetitorTop.place = 2
                }else {
                    matchCompetitorBottom.place = 2
                }
            }
            THIRD_PLACE -> {
                if(didTopWin()) {
                    matchCompetitorTop.place = 3
                }else {
                    matchCompetitorBottom.place = 3
                }
            }
        }

        when(loserNextIndex) {
            SECOND_PLACE -> {
                if(!didTopWin()) {
                    matchCompetitorTop.place = 2
                }else {
                    matchCompetitorBottom.place = 2
                }
            }
            THIRD_PLACE -> {
                if(!didTopWin()) {
                    matchCompetitorTop.place = 3
                }else {
                    matchCompetitorBottom.place = 3
                }
            }
        }

        callback.matchComplete(index)
    }
    
    fun didTopWin(): Boolean {
        return matchCompetitorTop.winner
    }
    
    private fun getWinner(): MatchCompetitor? {
        return when {
            matchCompetitorTop.winner -> {
                matchCompetitorTop
            }
            matchCompetitorBottom.winner -> {
                matchCompetitorBottom
            }
            else -> {
                null
            }
        }
    }
    
    private fun getLoser(): MatchCompetitor? {
        return when {
            matchCompetitorTop.winner -> {
                matchCompetitorBottom
            }
            matchCompetitorBottom.winner -> {
                matchCompetitorTop
            }
            else -> {
                null
            }
        }
    }

    fun getWinningCompetitor(): Competitor? {
        return getWinner()?.competitor
    }

    fun getLosingCompetitor(): Competitor? {
        return getLoser()?.competitor
    }

    fun isMatchReady(): Boolean {
        return !matchCompetitorTop.isPlaceholder() && !matchCompetitorBottom.isPlaceholder()
    }

    fun isMatchFinished(): Boolean {
        return winCondition.isNotEmpty()
    }

    fun getCompetitorTop(): MatchCompetitor {
        return matchCompetitorTop
    }

    fun getCompetitorBottom(): MatchCompetitor {
        return matchCompetitorBottom
    }

    fun addCompetitor(competitor: Competitor) {
        when {
            matchCompetitorTop.competitor.isPlaceholder() -> {
                matchCompetitorTop = MatchCompetitor(competitor, MatchCompetitor.Color.Red)
            }
            matchCompetitorBottom.competitor.isPlaceholder() -> {
                matchCompetitorBottom = MatchCompetitor(competitor, MatchCompetitor.Color.Blue)
            }
        }
    }

    fun getTitle(): String {
        when {
            winnerNextIndex == FIRST_PLACE && loserNextIndex == SECOND_PLACE -> return SLApplication.getContext().getString(R.string.winner_bracket)
            winnerNextIndex == THIRD_PLACE -> return SLApplication.getContext().getString(R.string.third_place_bracket)
        }
        return ""
    }

    interface MatchCallback {
        fun matchComplete(matchIndex: Int)
    }

}