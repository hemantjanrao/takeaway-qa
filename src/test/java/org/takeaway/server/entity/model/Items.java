package org.takeaway.server.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Items {

    @Expose
    @SerializedName("items")
    public List<Media> items;

    public Items() {
        this.items = new ArrayList<>();
    }

    /**
     * Method to set media
     *
     * @param mediaId   int
     * @param mediaType String
     * @param comment   String
     */
    public void setMedia(int mediaId, String mediaType, String comment) {
        var media = new Media(mediaId, mediaType.strip(), comment );
        items.add(media);
    }
}


