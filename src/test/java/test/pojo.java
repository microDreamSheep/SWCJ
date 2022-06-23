package test;

import com.midream.sheep.swcj.annotation.WebSpider;

public interface pojo {
    @WebSpider("getit")
    String[] getit(String i,String in);

}
