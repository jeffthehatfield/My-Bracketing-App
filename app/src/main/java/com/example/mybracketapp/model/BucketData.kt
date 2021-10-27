package com.example.mybracketapp.model

import android.util.Log
import com.example.mybracketapp.view.bracket.BracketFragment

class BucketData(
    private val skillLevel: String,
    private val minWeight: Int,
    private val maxWeight: Int,
    private val minAge : Int,
    private val maxAge : Int,
    private val gender: String) : MatchData.MatchCallback {

    private var competitorsList: ArrayList<Competitor> = ArrayList()
    private var columns: ArrayList<ColumnData> = ArrayList()
    var fragment: BracketFragment? = null

    fun numberOfCompetitors(): Int {
        return competitorsList.size
    }

    fun addCompetitor(competitor: Competitor) {
        competitorsList.add(competitor)
        Log.d("Jeff", "Added ${competitor.name()} to ${toString()}")
        generateMatches()
    }

    fun doesCompetitorMatchBucket(competitor: Competitor): Boolean {
        return competitor.skillLevel == skillLevel
                && competitor.weight in (minWeight) until maxWeight+1
                && competitor.age in (minAge) until maxAge+1
                && competitor.gender == gender
    }

    fun removeCompetitor(competitor: Competitor) {
        competitorsList.remove(competitor)
        generateMatches()
    }

    private fun generateMatches() {
        columns = ArrayList()
        var matches: ArrayList<MatchData> = ArrayList()
        when(competitorsList.size) {
            1 -> {
                matches.add(MatchData(0, this,-1, -1, competitorsList[0], Competitor()))
                columns.add(ColumnData(matches))
            }
            2 -> {
                matches.add(MatchData(0, this, -1, -1, competitorsList[0], competitorsList[1]))
                matches.add(MatchData(1, this, -1, -1, competitorsList[0], competitorsList[1]))
                columns.add(ColumnData(matches))

                matches= ArrayList()
                matches.add(MatchData(2, this, -1, -1, competitorsList[0], competitorsList[1]))
                columns.add(ColumnData(matches))
            }
            3 -> {
                matches.add(MatchData(0, this, 1,  MatchData.THIRD_PLACE, competitorsList[0], competitorsList[1]))
                columns.add(ColumnData(matches))

                matches= ArrayList()
                matches.add(MatchData(1, this, MatchData.FIRST_PLACE,  MatchData.SECOND_PLACE, competitorsList[2], Competitor()))
                columns.add(ColumnData(matches))
            }

            4 -> {
                matches.add(MatchData(0, this, 2, 3, competitorsList[0], competitorsList[1]))
                matches.add(MatchData(1, this, 2, 3, competitorsList[2], competitorsList[3]))
                columns.add(ColumnData(matches))

                matches = ArrayList()
                matches.add(MatchData(2, this, MatchData.FIRST_PLACE, MatchData.SECOND_PLACE))
                matches.add(MatchData(3, this, MatchData.THIRD_PLACE, MatchData.EMPTY))
                columns.add(ColumnData(matches))
            }

            8 -> {
                //Quarter Finals
                matches.add(MatchData(0, this, 4, -1, competitorsList[0], competitorsList[1]))
                matches.add(MatchData(1, this, 4, -1, competitorsList[2], competitorsList[3]))
                matches.add(MatchData(2, this,5, -1, competitorsList[4], competitorsList[5]))
                matches.add(MatchData(3, this,5, -1, competitorsList[6], competitorsList[7]))
                columns.add(ColumnData(matches))

                //Semi Finals
                matches = ArrayList()
                matches.add(MatchData(4, this, 6, 7, Competitor(), Competitor()))
                matches.add(MatchData(5, this, 6, 7, Competitor(), Competitor()))
                columns.add(ColumnData(matches))

                //Finals
                matches = ArrayList()
                matches.add(MatchData(6, this, MatchData.FIRST_PLACE, MatchData.SECOND_PLACE))
                matches.add(MatchData(7, this, MatchData.THIRD_PLACE, MatchData.EMPTY))
                columns.add(ColumnData(matches))

                columns.add(ColumnData(ArrayList()))
            }
        }
    }

    override fun matchComplete(matchIndex: Int) {
        columns.forEach { column ->
            column.getMatches().forEachIndexed { _, matchData ->
                if(matchData.index == matchIndex){
                    columns.forEachIndexed { columnIndex, nextColumn ->
                        nextColumn.getMatches().forEachIndexed { nextIndex, nextMatchData ->
                            if(nextMatchData.index == matchData.winnerNextIndex){
                                nextMatchData.addCompetitor(matchData.getWinningCompetitor()!!)
                            }
                            if(nextMatchData.index == matchData.loserNextIndex){
                                nextMatchData.addCompetitor(matchData.getLosingCompetitor()!!)
                            }
                        }
                    }
                }
            }
        }
        fragment?.update()
    }

    fun getColumns(): ArrayList<ColumnData> {
        return columns
    }

    override fun toString() :String {
        return "Bucket(${competitorsList.size}): ${if(minAge >= 30) "Master" else if(minAge>16) "Adult" else "Teen"} $gender $skillLevel Weight:$minWeight${if(maxWeight>900) "+" else "-$maxWeight"}"
    }

    fun specString(): String {
        return "Bucket(${competitorsList.size}): Age:$minAge-$maxAge $gender $skillLevel Weight:$minWeight-$maxWeight"
    }

    fun titleString(): String {
        return "${if(minAge >= 30) "Master" else if(minAge>16) "Adult" else "Teen"} $gender $skillLevel\nWeight:$minWeight${if(maxWeight>900) "+" else "-$maxWeight"}"
    }
}