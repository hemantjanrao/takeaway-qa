package org.takeaway.server.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Media {

    @Expose
    @SerializedName("media_type")
    private String media_type;

    @Expose
    @SerializedName("media_id")
    private int media_id;

    @Expose
    @SerializedName("comment")
    private String comment;

    public Media(int media_id, String media_type, String comment) {
        this.media_type = media_type;
        this.media_id = media_id;
        this.comment = comment;
    }
}