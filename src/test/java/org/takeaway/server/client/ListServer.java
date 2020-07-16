package org.takeaway.server.client;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.takeaway.constants.ListEndpoints;
import org.takeaway.core.base.AbstractService;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.server.entity.model.Items;
import org.takeaway.server.entity.model.Media;
import org.takeaway.server.entity.response.AddUpdateItemResponse;
import org.takeaway.server.entity.response.GetListResponse;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class ListServer extends AbstractService {

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

    /**
     * Method to get the list
     *
     * @param listId int
     * @return Response
     */
    public GetListResponse getList(int listId) {

        Response response = getRequest(ListEndpoints.GET_LIST.getUrl(listId));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        return TestHelper.deserializeJson(response, GetListResponse.class);
    }

    /**
     * Method to check whether list exists or not
     *
     * @param listId int
     * @return Boolean
     */
    public boolean doesListExists(int listId) {

        Response response = getRequest(ListEndpoints.GET_LIST.getUrl(listId));
        return response.statusCode() == HttpStatus.SC_OK;
    }

    /**
     * Method to get provided list size
     *
     * @param listId int
     * @return int
     */
    public int getListSize(int listId){

        Response response = getRequest(ListEndpoints.GET_LIST.getUrl(listId));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        return TestHelper.deserializeJson(response, GetListResponse.class).results.size();
    }

    /**
     * Method to remove all the items from the list
     *
     * @param listId int
     * @return Response
     */
    public Response clearList(int listId) {
        return getRequest(ListEndpoints.CLEAR.getUrl(listId));
    }

    /**
     * Method to create the list
     *
     * @param list ListModel
     * @return Response
     */
    public ListResponse createList(String list) {

        Response response = postRequest(ListEndpoints.LIST.getUrl(), list);
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_CREATED);

        return TestHelper.deserializeJson(response, ListResponse.class);
    }

    /**
     * Method to delete the list
     *
     * @param listId ListModel
     * @return Response
     */
    public Response deleteList(int listId) {

        return deleteRequest(ListEndpoints.DELETE.getUrl(listId));
    }

    /**
     * Method to update the list
     *
     * @param listId      int
     * @param updatedList UpdateListModel
     * @return Response
     */
    public Response updateList(int listId, String updatedList) {

        return updateRequest(ListEndpoints.GET_LIST.getUrl(listId), updatedList);
    }

    /**
     * Method to add item to the list
     *
     * @param listId   int
     * @param itemList ItemsModel
     * @return Response
     */
    public AddUpdateItemResponse addItemsToList(int listId, Items itemList) {

        Response response = postRequest(ListEndpoints.ITEMS.getUrl(listId), TestHelper.serializeToJson(itemList));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        return TestHelper.deserializeJson(response, AddUpdateItemResponse.class);
    }

    /**
     * Method to update item to the list
     *
     * @param listId   int
     * @param itemList ItemsModel
     * @return Response
     */
    public AddUpdateItemResponse updateItemsToList(int listId, Items itemList) {

        Response response = updateRequest(ListEndpoints.ITEMS.getUrl(listId), TestHelper.serializeToJson(itemList));
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);

        return TestHelper.deserializeJson(response, AddUpdateItemResponse.class);
    }

    /**
     * Method to update item to the list
     *
     * @param listId   int
     * @param itemList ItemsModel
     * @return Response
     */
    public Response deleteItemFromList(int listId, Items itemList) {

        return deleteRequest(ListEndpoints.ITEMS.getUrl(listId), TestHelper.serializeToJson(itemList));
    }

    /**
     * Method to check whether item exists
     *
     * @param listId int
     * @param itemList List<Media>
     * @return Boolean
     */
    public boolean doesItemsExistsInList(int listId, List<Media> itemList){

        List<Boolean> status = new ArrayList<>();

        for (Media item : itemList) {
            Response response = getRequest(ListEndpoints.ITEM_STATUS.getUrl(listId, item.getMedia_id(), item.getMedia_type()));
            int statusCode = response.statusCode();

            if (statusCode == HttpStatus.SC_OK) {
                status.add(true);
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                status.add(false);
            } else {
                throw new IllegalStateException("Unexpected return code");
            }
        }
        return !status.contains(false);
    }
}
