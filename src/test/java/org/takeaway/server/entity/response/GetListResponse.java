package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListResponse {

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("results")
    public List<MediaResponse> results;
}
