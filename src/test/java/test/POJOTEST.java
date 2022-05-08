package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface POJOTEST {
    @WebSpider("get")
    image[] getIt(int i);
}
