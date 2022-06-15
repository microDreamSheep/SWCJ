package test;

import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.pojo.image;

public interface POJOTEST {
    @WebSpider("get")
    image[] getIt(int i);
}
