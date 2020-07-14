package org.takeaway.core.base;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.takeaway.core.util.Environment;
import org.takeaway.core.util.PropertyUtils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public abstract class AbstractService extends BaseService {

    protected static Logger log = LogManager.getLogger(AbstractService.class);
    static String req_toke;

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
     * @return RequestSpecification
     */
    protected RequestSpecification getRequestSpec() {

        return RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType("application/json")
                .accept("*/*")
                .header("Authorization", "Bearer " + PropertyUtils.get(Environment.WRITE_ACCESS_TOKEN))
                .accept("application/json");
    }

    private String getRequestToken() {
        return RestAssured.given()
                .contentType("application/json;charset=utf-8")
                .header("Authorization", "Bearer " + PropertyUtils.get(Environment.ACCESS_TOKEN))
                .redirects().follow(true)
                .post("/4/auth/request_token")
                .then()
                .extract().response().getBody().jsonPath().get("request_token");
    }

    private void login() {

        Map<String, String> formParams = new HashMap<>();
        formParams.put("username", "hemantjanrao");
        formParams.put("password", "Test1234");

        Response response = given()
                .baseUri("https://www.themoviedb.org")
                .get("/login");

        Response post1 = given()
                .baseUri("https://www.themoviedb.org")
                .cookies(response.getCookies())
                .post("/pusher/auth");


        Response Response = given()
                .baseUri("https://www.themoviedb.org")
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .redirects().follow(true).redirects().max(100)
                .cookies(post1.getCookies())
                .formParams(formParams)
                .log().all()
                .post("/login")
                .then()
                .extract().response();

        System.out.println("LOGIN STATUS "+ response.statusCode());

        req_toke = getRequestToken();
        //req_toke = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE1OTQ3Mjc5MjcsInNjb3BlcyI6WyJwZW5kaW5nX3JlcXVlc3RfdG9rZW4iXSwiZXhwIjoxNTk0NzI4ODI3LCJqdGkiOjIyMTgxMzUsImF1ZCI6IjlhM2U1YzIxNjY3OTQ0NTYxOWFmZjU0YTA0OTQ2NjA1IiwicmVkaXJlY3RfdG8iOiJodHRwOlwvXC93d3cudGhlbW92aWVkYi5vcmdcLyIsInZlcnNpb24iOjF9.Rdy9Ks2ReBYSn_WQojH2qDQrKzlVYhOijt_2KO2095I";

        Response rsp = given()
                .baseUri("https://www.themoviedb.org")
                .redirects().follow(true).redirects().max(100)
                .cookies(Response.getCookies())
                .queryParam("request_token", req_toke)
                .log().all()
                .get("/auth/access")
                .then()
                .extract().response();

        System.out.println("STATUS  " + rsp.statusCode());

//        req_toke = getRequestToken();
//
//        System.out.println("REQ TOKEN " + req_toke);
//
//        Map<String, String> formParams1 = new HashMap<>();
//        formParams1.put("request_token", req_toke);
//        //formParams1.put("time", String.valueOf(System.currentTimeMillis()));
//        formParams1.put("submit", "Approve");
//
//        Response post = given()
//                .baseUri("https://www.themoviedb.org")
//                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
//                .redirects().follow(true).redirects().max(100)
//                .cookies(rsp.getCookies())
//                .formParams(formParams1)
//                .log().all()
//                .post("/auth/access/approve");
//
//        System.out.println("IS APPROVED " + post.statusCode());
    }

    private String getNewAccessToken() {
        JSONObject json = new JSONObject();
        json.put("request_token", req_toke);

        Response authorization = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + PropertyUtils.get(Environment.ACCESS_TOKEN))
                .body(json)
                .post("/4/auth/access_token")
                .then()
                //.log().all()
                .extract().response();

        return authorization.getBody().jsonPath().get("access_token");
    }

    protected Response getRequest(String url) {
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
        return request(Method.PATCH, body, url);
    }
}
