package org.takeaway.server.client;

import io.restassured.response.Response;
import org.takeaway.core.base.AbstractService;
import org.takeaway.server.entity.model.ItemsModel;
import org.takeaway.server.entity.model.ListModel;
import org.takeaway.server.entity.model.MediaModel;
import org.takeaway.server.entity.model.UpdateListModel;

public class ListServer extends AbstractService {

    private static final String LIST_URL = "/list";

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
        log.info("Getting the list");
        return getRequest(LIST_URL + "/" + listId);
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
     * @param listId      int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response addItemToList(int listId, ItemsModel itemList) {
        return postRequest(LIST_URL + "/" + listId+"/items", itemList);
    }

    /**
     * Method to update item to the list
     *
     * @param listId      int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response updateItemFromList(int listId, ItemsModel itemList) {
        return updateRequest(LIST_URL + "/" + listId+"/items?language=en-US", itemList);
    }

    /**
     * Method to update item to the list
     *
     * @param listId      int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response deleteItemFromList(int listId, ItemsModel itemList) {
        return deleteRequest(LIST_URL + "/" + listId+"/items", itemList);
    }

    /**
     * To create Media
     *
     * @param mediaId int
     * @param mediaType String
     * @return MediaModel
     */
    public MediaModel createMedia(int mediaId, String mediaType, String comment){
        MediaModel media = new MediaModel();

        media.setMedia_id(mediaId);
        media.setMedia_type(mediaType);
        media.setComment(comment);

        return media;
    }


}
