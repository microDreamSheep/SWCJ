package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface pojo {
    @WebSpider("gethtml")
    test.image[] getIt(String i,String in);

}
