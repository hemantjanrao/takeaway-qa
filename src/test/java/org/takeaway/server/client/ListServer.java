package org.takeaway.server.client;

import io.restassured.response.Response;
import org.takeaway.core.base.AbstractService;
import org.takeaway.server.entity.model.ItemsModel;
import org.takeaway.server.entity.model.ListModel;
import org.takeaway.server.entity.model.MediaModel;
import org.takeaway.server.entity.model.UpdateListModel;

import java.util.ArrayList;
import java.util.List;

public class ListServer extends AbstractService {

    private static final String LIST_URL = "/list";

    private static final List<MediaModel> mediaList = new ArrayList<>();
    ;

    /**
     * Constructor
     *
     * @param host       Host Name
     * @param protocol   Protocol
     * @param apiVersion API version number
     */
    public ListServer(String host, String protocol, int apiVersion) {
        super(host, protocol, apiVersion);
    }

    public Response getList(int listId) {
        return getRequest(LIST_URL + "/" + listId);
    }

    public Response clearList(int listId) {
        return getRequest(LIST_URL + "/" + listId + "/clear");
    }

    /**
     * Method to create the list
     *
     * @param list ListModel
     * @return Response
     */
    public Response createList(ListModel list) {
        return postRequest(LIST_URL, list);
    }

    /**
     * Method to delete the list
     *
     * @param listId ListModel
     * @return Response
     */
    public Response deleteList(int listId) {
        return deleteRequest(LIST_URL + "/" + listId);
    }

    /**
     * Method to update the list
     *
     * @param listId      int
     * @param updatedList UpdateListModel
     * @return Response
     */
    public Response updateList(int listId, UpdateListModel updatedList) {
        return updateRequest(LIST_URL + "/" + listId, updatedList);
    }

    /**
     * Method to add item to the list
     *
     * @param listId   int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response addItemToList(int listId, ItemsModel itemList) {
        Response response = postRequest(LIST_URL + "/" + listId + "/items", itemList);
        getItems().clear();
        return response;
    }

    /**
     * Method to update item to the list
     *
     * @param listId   int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response updateItemFromList(int listId, ItemsModel itemList) {
        return updateRequest(LIST_URL + "/" + listId + "/items", itemList);
    }

    /**
     * Method to update item to the list
     *
     * @param listId   int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response deleteItemFromList(int listId, ItemsModel itemList) {
        return deleteRequest(LIST_URL + "/" + listId + "/items", itemList);
    }

    /**
     * Method to set media
     *
     * @param mediaId int
     * @param mediaType String
     * @param comment String
     */
    public void setMedia(int mediaId, String mediaType, String comment) {
        MediaModel media = new MediaModel();

        media.setMedia_id(mediaId);
        media.setMedia_type(mediaType);
        media.setComment(comment);

        mediaList.add(media);
    }

    /**
     * Method to get list of items
     *
     * @return List<MediaModel>
     */
    public List<MediaModel> getItems() {
        return mediaList;
    }
}
