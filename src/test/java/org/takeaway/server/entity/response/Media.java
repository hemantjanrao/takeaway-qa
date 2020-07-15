package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Media {
    @Expose
    @SerializedName("media_type")
    public String media_type;

    @Expose
    @SerializedName("media_id")
    public int media_id;

    @Expose
    @SerializedName("success")
    public boolean success;
}
