package org.takeaway.server.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListResponse {

    @Expose
    @SerializedName("status_message")
    public String status_message;

    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("success")
    public boolean success;

    @Expose
    @SerializedName("status_code")
    public int status_code;


}
