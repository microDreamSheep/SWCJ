package com.midream.sheep;

import com.midream.sheep.swcj.Annotation.WebSpider;

public interface WebTest {
    @WebSpider("getImage")
    Image[] a(String count);

}
