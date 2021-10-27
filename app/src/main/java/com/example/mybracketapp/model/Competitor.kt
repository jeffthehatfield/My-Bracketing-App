package com.example.mybracketapp.model

import android.os.Parcel
import android.os.Parcelable
import com.example.mybracketapp.R
import com.example.mybracketapp.SLApplication
import com.google.gson.annotations.SerializedName

data class Competitor(@SerializedName("id") val id: Int,
                      @SerializedName("first_name") val firstName: String,
                      @SerializedName("last_name") val lastName: String,
                      @SerializedName("weight") val weight: Int,
                      @SerializedName("gender") val gender: String,
                      @SerializedName("age") val age: Int,
                      @SerializedName("skill_level") val skillLevel: String) : Parcelable {

    constructor() : this(PLACEHOLDER_ID, SLApplication.getContext().getString(R.string.to_be_determined_abbr), "", 0, "", 0, "")

    fun isPlaceholder(): Boolean {
        return id == PLACEHOLDER_ID
    }


    fun name():String {
        return "$firstName $lastName"
    }

    override fun toString(): String {
        return name()
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeInt(weight)
        parcel.writeString(gender)
        parcel.writeInt(age)
        parcel.writeString(skillLevel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Competitor> {
        private val PLACEHOLDER_ID = -1

        override fun createFromParcel(parcel: Parcel): Competitor {
            return Competitor(parcel)
        }

        override fun newArray(size: Int): Array<Competitor?> {
            return arrayOfNulls(size)
        }
    }
}