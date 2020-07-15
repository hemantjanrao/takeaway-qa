package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddItemResponse {

    @Expose
    @SerializedName("success")
    public boolean success;

    @Expose
    @SerializedName("status_code")
    public int status_code;

    @Expose
    @SerializedName("results")
    public List<Media> results;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public List<Media> getResults() {
        return results;
    }

    public void setResults(List<Media> results) {
        this.results = results;
    }
}

class Media{
    @Expose
    @SerializedName("media_type")
    public String media_type;

    @Expose
    @SerializedName("media_id")
    public int media_id;

    @Expose
    @SerializedName("success")
    public boolean success;

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
