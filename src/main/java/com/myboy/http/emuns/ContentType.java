package com.myboy.http.emuns;

public enum ContentType {
    TXT_HTML("text/html"), JSON("application/json");

    private final String contentType;

    ContentType(String contentType){
        this.contentType = contentType;
    }

    public String getType() {
        return contentType;
    }
}
