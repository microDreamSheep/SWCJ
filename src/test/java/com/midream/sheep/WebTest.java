package com.midream.sheep;

import com.midream.sheep.swcj.Annotation.WebSpider;

public interface WebTest {
    @WebSpider("getHtml")
    String[] test(String count,String type,String ad);
    @WebSpider("more")
    String[] ad(int i);
}
