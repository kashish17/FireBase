
package com.example.kashish.movies.TvTrailers;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TvTrailersResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Result> mResults;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

}
