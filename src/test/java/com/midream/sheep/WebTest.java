package com.midream.sheep;

import com.midream.sheep.pojo.Novel;
import com.midream.sheep.swcj.Annotation.WebSpider;

public interface WebTest {
    @WebSpider("getHtml")
    String[] a(String count);

}
