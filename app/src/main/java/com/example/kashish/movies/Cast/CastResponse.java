
package com.example.kashish.movies.Cast;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class CastResponse {

    @SerializedName("cast")
    private List<Cast> mCast;

    @SerializedName("id")
    private Long mId;

    public List<Cast> getCast() {
        return mCast;
    }

    public void setCast(List<Cast> cast) {
        mCast = cast;
    }



    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

}
