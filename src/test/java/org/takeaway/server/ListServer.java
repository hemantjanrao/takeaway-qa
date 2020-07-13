package org.takeaway.server;

import io.restassured.response.Response;
import org.takeaway.core.base.AbstractService;

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

}
