package com.midream.sheep.swcj.core.executetool.execute.jsoup.pojo;

/**
 * @author midreamsheep
 */
public class Pa {
    private String not;
    private int allstep;
    private int step;
    private String element;
    private String value;

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }

    public int getAllstep() {
        return allstep;
    }

    public void setAllstep(int allstep) {
        this.allstep = allstep;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pa{" +
                "not='" + not + '\'' +
                ", allstep=" + allstep +
                ", step=" + step +
                ", element='" + element + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
