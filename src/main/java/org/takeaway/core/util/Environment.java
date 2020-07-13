package org.takeaway.core.util;

public enum Environment {
    API_URL("api.base.url"),
    API_PROTOCOL("api.protocol"),
    API_VERSION("api.version"),
    ACCESS_TOKEN("api.read.access.token");

    private final String key;

    Environment(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
