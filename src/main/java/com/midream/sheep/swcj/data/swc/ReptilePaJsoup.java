package com.midream.sheep.swcj.data.swc;
/**
 * @author midreamSheep
 * jsoup爬虫的核心类
 * */
public class ReptilePaJsoup {
    //jsoup格式
    private String paText;

    public ReptilePaJsoup() {
    }

    public String getPaText() {
        return paText;
    }

    public void setPaText(String paText) {
        this.paText = paText;
    }

    @Override
    public String toString() {
        return "ReptilePaJsoup{" +
                "paText='" + paText + '\'' +
                '}';
    }
}
