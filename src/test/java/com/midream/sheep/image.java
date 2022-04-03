package com.midream.sheep;

/**
 * @author midreamsheep
 */
public class image {
    String url;
    String name;

    @Override
    public String toString() {
        return "image{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
