package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
}

@Getter
@Setter
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
}
