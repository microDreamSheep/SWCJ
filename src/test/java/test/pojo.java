package test;

import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.pojo.image;

public interface pojo {
    @WebSpider("getHtml")
    image[] getIt(int i);
}
