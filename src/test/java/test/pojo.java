package test;

import com.midream.sheep.swcj.annotation.WebSpider;

import java.awt.*;

public interface pojo {
    @WebSpider("getHtml")
    image[] getIt(int i);
}
