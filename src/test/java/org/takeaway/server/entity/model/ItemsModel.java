package org.takeaway.server.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemsModel {

    public ItemsModel() {
        this.items = new ArrayList<>();
    }

    @Expose
    @SerializedName("items")
    public List<MediaModel> items;


    /**
     * Method to set media
     *
     * @param mediaId   int
     * @param mediaType String
     * @param comment   String
     */
    public void setMedia(int mediaId, String mediaType, String comment) {
        MediaModel media = new MediaModel();

        media.setMedia_id(mediaId);
        media.setMedia_type(mediaType);
        media.setComment(comment);

        items.add(media);
    }
}


