package org.takeaway.server.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListModel {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("iso_639_1")
    private String iso_639_1;

    @Expose
    @SerializedName("description")
    private String description;
}
