package com.midream.sheep.SWCJ.data.swc;

public class ReptilePaJsoup {
    private String text;
    private String reg;
    private String paText;

    public ReptilePaJsoup() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ReptilePaJsoup{" +
                "text='" + text + '\'' +
                ", reg='" + reg + '\'' +
                ", paText='" + paText + '\'' +
                '}';
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getPaText() {
        return paText;
    }

    public void setPaText(String paText) {
        this.paText = paText;
    }
}
