package org.takeaway.server.client;

import io.restassured.response.Response;
import org.takeaway.core.base.AbstractService;
import org.takeaway.server.entity.model.ListModel;
import org.takeaway.server.entity.model.UpdateListModel;

public class ListServer extends AbstractService {

    private static final String LIST_URL = "/list";

    /**
     * Constructor
     *
     * @param host     Host Name
     * @param protocol Protocol
     * @param apiVersion API version number
     */
    public ListServer(String host, String protocol, int apiVersion) {
        super(host, protocol, apiVersion);
    }

    public Response getList(int listId){

        log.info("Getting the list");

        return getRequest(LIST_URL +"/"+listId);
    }

    public Response createList(ListModel list){

        return postRequest(LIST_URL, list);
    }

    public Response updateList(int listId, UpdateListModel updatedList){
        return updateRequest(LIST_URL+"/"+listId, updatedList);
    }
}
