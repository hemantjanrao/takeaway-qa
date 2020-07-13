package org.takeaway.core.base;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;

public abstract class AbstractService extends BaseService {

    protected static Logger log = LogManager.getLogger(AbstractService.class);

    /**
     * Constructor
     * @param host Host Name
     * @param protocol Protocol
     */
    public AbstractService(String protocol, String host, int apiVersion) {
        rootURL = protocol +"://"+ host+"/"+apiVersion;
    }

    /**
     * @return RequestSpecification
     */
    protected RequestSpecification getRequestSpec(){
        return RestAssured.given()
                .baseUri(rootURL)
                .relaxedHTTPSValidation()
                .contentType("application/json;charset=utf-8")
                .header("Authorization", "Bearer "+getRequestToken())
                .accept("application/json");
    }

    private String getRequestToken(){
        return RestAssured.given()
                .baseUri(rootURL+"/auth/request_token")
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Authorization", "Bearer "+PropertyUtils.get(Environment.ACCESS_TOKEN))
                .redirects().follow(true)
                .post()
                .then()
                .extract().response().getBody().jsonPath().get("request_token");
    }

    protected Response getRequest(String url){
        return request(Method.GET, url);
    }

    /**
     * @param body Body
     * @param url URL
     * @return Response
     */
    protected Response postRequest(String url, Object body) {
        return request(Method.POST, body, url);
    }
}
