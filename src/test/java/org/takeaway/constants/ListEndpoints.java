package org.takeaway.constants;

public enum ListEndpoints {

    CLEAR("/list/%d/clear"),
    DELETE("/list/%d"),
    GET_LIST("/list/%d"),
    ITEMS("/list/%d/items"),
    LIST("/list");

    private final String url;

    ListEndpoints(String url){
        this.url = url;
    }

    public String getUrl(Object... params) {
        return String.format(url, params);
    }
}
