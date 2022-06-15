package test;

public class image {
    String url;
    String name;

    public image() {
    }

    public String toString() {
        return "image{url='" + this.url + '\'' + ", name='" + this.name + '\'' + '}';
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}