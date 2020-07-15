package org.takeaway.server.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaModel {
    @Expose
    @SerializedName("media_type")
    private String media_type;

    @Expose
    @SerializedName("media_id")
    private int media_id;

    @Expose
    @SerializedName("comment")
    private String comment;
}