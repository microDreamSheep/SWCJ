package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface pojo {
    @WebSpider("getit")
    image[] gethtml(String i);
}
