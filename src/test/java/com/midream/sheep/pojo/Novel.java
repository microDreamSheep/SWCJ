package com.midream.sheep.pojo;

/**
 * @author midreamsheep
 */
public class Novel {
    private String id;
    private String title;
    private String writer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return id+writer+title;
    }
}
