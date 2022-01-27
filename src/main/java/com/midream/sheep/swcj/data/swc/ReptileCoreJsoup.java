package com.midream.sheep.swcj.data.swc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author midreamsheep
 */
public class ReptileCoreJsoup {
    //jsoup策略
    private List<ReptilePaJsoup> jsoup;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReptilePaJsoup> getJsoup() {
        return jsoup;
    }

    public void setJsoup(List<ReptilePaJsoup> jsoup) {
        this.jsoup = jsoup;
    }
    public void addJsoup(ReptilePaJsoup data){
        if(jsoup==null){
            jsoup = new ArrayList<>();
        }
        jsoup.add(data);
    }
}
