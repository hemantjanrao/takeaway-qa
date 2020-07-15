package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaResponse {


    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("media_type")
    public String media_type;
}
