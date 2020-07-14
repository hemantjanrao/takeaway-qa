package org.takeaway.server.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemsModel {
    @Expose
    @SerializedName("items")
    public List<MediaModel> items;

    public List<MediaModel> getItems() {
        return items;
    }

    public void setItems(List<MediaModel> items) {
        this.items = items;
    }
}


