
package com.example.kashish.movies.TvCastDetils;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TvCastResponse {

    @SerializedName("cast")
    private List<Cast> mCast;
    @SerializedName("crew")
    private List<Object> mCrew;
    @SerializedName("id")
    private Long mId;

    public List<Cast> getCast() {
        return mCast;
    }

    public void setCast(List<Cast> cast) {
        mCast = cast;
    }

    public List<Object> getCrew() {
        return mCrew;
    }

    public void setCrew(List<Object> crew) {
        mCrew = crew;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

}
