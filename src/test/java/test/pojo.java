package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface pojo {
    @WebSpider("getHtml")
    test.image[] getIt(String i,String in);

}
