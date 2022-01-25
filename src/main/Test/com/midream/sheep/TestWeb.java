package com.midream.sheep;

import com.midream.sheep.SWCJ.Annotation.WebSpider;

public interface TestWeb {
    @WebSpider("getHtml")
    String[] getdata();
}
