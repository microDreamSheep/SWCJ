package com.midream.sheep;

import com.midream.sheep.swcj.Annotation.WebSpider;

public interface WEBSPI {
    @WebSpider("getHtml")
    String[] a(String count);
}
