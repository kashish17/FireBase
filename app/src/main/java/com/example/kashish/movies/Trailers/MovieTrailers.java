
package com.example.kashish.movies.Trailers;

import java.util.List;


import com.google.gson.annotations.Expose;


@SuppressWarnings("unused")
public class MovieTrailers {

    @Expose
    private Long id;
    @Expose
    private List<Result> results;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
