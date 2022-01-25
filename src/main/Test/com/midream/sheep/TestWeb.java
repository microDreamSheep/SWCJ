package com.midream.sheep;

import com.midream.sheep.swsj.Annotation.WebSpider;

public interface TestWeb {
    @WebSpider("getHtml")
    String[] getdata();
}
