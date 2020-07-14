package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaResponse {


    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("media_type")
    public String media_type;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
}
