package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface pojo {
    @WebSpider("getHtml")
    image[] getIt(int i,String in);
}
