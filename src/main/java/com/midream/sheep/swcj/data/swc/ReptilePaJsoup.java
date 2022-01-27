package com.midream.sheep.swcj.data.swc;
/**
 * @author midreamSheep
 * jsoup爬虫的核心类
 * */
public class ReptilePaJsoup {
    //jsoup格式
    private String paText;
    private String[] not;
    public ReptilePaJsoup() {
    }

    public String[] getNot() {
        return not;
    }

    public void setNot(String[] not) {
        this.not = not;
    }

    public String getPaText() {
        return paText;
    }

    public void setPaText(String paText) {
        this.paText = paText;
    }

}
