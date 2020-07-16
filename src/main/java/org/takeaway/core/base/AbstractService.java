package org.takeaway.core.base;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;

public abstract class AbstractService extends BaseService {

    protected final Logger log = Logger.getLogger(getClass());

    /**
     * Constructor
     *
     * @param host     Host Name
     * @param protocol Protocol
     */
    public AbstractService(String protocol, String host, int apiVersion) {
        rootURL = protocol + "://" + host + "/" + apiVersion;
        RestAssured.baseURI = rootURL;
    }

    /**
     * Generic request specification for every api call
     *
     * @return RequestSpecification
     */
    protected RequestSpecification getRequestSpec() {

        return RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType("application/json")
                .accept("*/*")
                .header("Authorization", "Bearer " + PropertyUtils.get(Environment.WRITE_ACCESS_TOKEN))
                .accept("*/*");
    }

    /**
     * Get request
     *
     * @param url String
     * @return Response
     */
    protected Response getRequest(String url) {
        log.info(String.format("GET - %s", url));
        return request(Method.GET, url);
    }

    /**
     * Post request
     *
     * @param body Body
     * @param url  URL
     * @return Response
     */
    protected Response postRequest(String url, Object body) {
        log.info(String.format("POST - %s with Payload - %s", url, body));
        return request(Method.POST, body, url);
    }

    /**
     * Update request
     *
     * @param body Body
     * @param url  URL
     * @return Response
     */
    protected Response updateRequest(String url, Object body) {
        log.info(String.format("UPDATE - %s with Payload - %s", url, body));
        return request(Method.PUT, body, url);
    }

    /**
     * Delete request
     *
     * @param url URL
     * @return Response
     */
    protected Response deleteRequest(String url) {
        log.info(String.format("DELETE - %s", url));
        return request(Method.DELETE, url);
    }

    /**
     * Delete request with payload
     *
     * @param body Body
     * @param url  URL
     * @return Response
     */
    protected Response deleteRequest(String url, Object body) {
        log.info(String.format("DELETE - %s with Payload - %s", url, body));
        return request(Method.DELETE, body, url);
    }
}
