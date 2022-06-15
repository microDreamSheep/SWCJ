package com.midream.sheep.swcj.core.executetool.execute;

/**
 * @author midreamsheep
 * @description request type description class
 */
public enum SRequest {
    GET("GET"),POST("POST");

    private String value;

    SRequest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static SRequest get(String type){
        switch (type){
            case "GET":
                return GET;
            case "POST":
                return POST;
        }
        return null;
    }
}
