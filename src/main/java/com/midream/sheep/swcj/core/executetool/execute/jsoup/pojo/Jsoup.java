package com.midream.sheep.swcj.core.executetool.execute.jsoup.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author midreamsheep
 */
public class Jsoup {
    @Override
    public String toString() {
        return "Jsoup{" +
                "pas=" + Arrays.toString(pas) +
                ", name='" + name + '\'' +
                '}';
    }

    private Pa[] pas;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pa[] getPas() {
        return pas;
    }

    public void setPas(Pa[] pas) {
        this.pas = pas;
    }
}
