package org.takeaway.server.client;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.takeaway.constants.ListEndpoints;
import org.takeaway.core.base.AbstractService;
import org.takeaway.core.helper.TestHelper;
import org.takeaway.server.entity.response.AddUpdateItemResponse;
import org.takeaway.server.entity.response.GetListResponse;
import org.takeaway.server.entity.response.ListResponse;
import org.testng.Assert;

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

    public boolean doesListExists(int listId) {
        Response response = getRequest(ListEndpoints.GET_LIST.getUrl(listId));
        return response.statusCode() == HttpStatus.SC_OK;
    }

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
    public AddUpdateItemResponse addUpdateItemsToList(int listId, String itemList) {
        Response response = postRequest(ListEndpoints.ITEMS.getUrl(listId), itemList);

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
    public Response deleteItemFromList(int listId, String itemList) {
        return deleteRequest(ListEndpoints.ITEMS.getUrl(listId), itemList);
    }

}
