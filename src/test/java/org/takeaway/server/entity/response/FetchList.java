package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchList {

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("results")
    public List<MediaResponse> results;

    public List<MediaResponse> getResults() {
        return results;
    }

    public void setResults(List<MediaResponse> results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
