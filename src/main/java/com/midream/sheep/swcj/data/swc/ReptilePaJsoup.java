package com.midream.sheep.swcj.data.swc;
/**
 * @author midreamSheep
 * jsoup爬虫的核心类
 * */
public class ReptilePaJsoup {
    //jsoup格式
    private String paText;
    private String[] not;
    private int step;
    private String element;
    private int allStep;

    public int getAllStep() {
        return allStep;
    }

    public void setAllStep(int allStep) {
        this.allStep = allStep;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

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
