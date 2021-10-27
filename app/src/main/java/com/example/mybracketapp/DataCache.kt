package com.example.mybracketapp

import com.example.mybracketapp.model.BucketData
import com.example.mybracketapp.model.Competitor


class DataCache {

    companion object {
        private var instance: DataCache? = null
        fun getInstance(): DataCache {
            if (instance == null) {
                synchronized(DataCache::class.java) {
                    if (instance == null) {
                        instance = DataCache()
                    }
                }
            }
            return instance!!
        }
    }


    var skillLevels = arrayOf("Beginner", "Intermediate", "Expert")
    var weightRanges = arrayOf(120, 140, 160, 180, 200, 999)
    var ageRanges = arrayOf(17, 29, 999)
    var genders = arrayOf("Male", "Female")
    private var competitors: ArrayList<Competitor> = ArrayList()
    private var bucketData: ArrayList<BucketData> = ArrayList()
    var nonEmptyBucketData: ArrayList<BucketData> = ArrayList()

    fun setCompetitors(competitors: ArrayList<Competitor>) {
        this.competitors = competitors
        generateBuckets()
    }

    fun generateBuckets() {
        bucketData = ArrayList()
        nonEmptyBucketData = ArrayList()
        skillLevels.forEach { skillLevel ->
            weightRanges.forEachIndexed{ weightIndex, weight ->
                ageRanges.forEachIndexed { ageIndex, age ->
                    genders.forEach { gender ->
                        bucketData.add(
                            BucketData(
                            skillLevel,
                            if(weightIndex == 0) 0 else weightRanges[weightIndex-1]+1,
                            if(weightIndex == weightRanges.size-1) 999 else weight,
                            if(ageIndex == 0) 0 else ageRanges[ageIndex-1]+1,
                            if(ageIndex == ageRanges.size-1) 999 else age,
                            gender)
                        )
                    }
                }
            }
        }


        competitors.forEach { competitor ->
            bucketData.forEach { bucket ->
                if(bucket.doesCompetitorMatchBucket(competitor)){
                    bucket.addCompetitor(competitor)
                }
            }
        }

        bucketData.forEach { bucket ->
            if(bucket.numberOfCompetitors() > 0){
                nonEmptyBucketData.add(bucket)
            }
        }

//            Log.d("Jeff", "----------------------------Final Buckets----------------------------")
//            nonEmptyCompetitorBuckets.forEach { bucket ->
//                Log.d("Jeff", "$bucket")
//            }

    }

}