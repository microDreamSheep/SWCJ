package test;

import com.midream.sheep.swcj.annotation.Param;
import com.midream.sheep.swcj.annotation.RequestType;
import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.core.executetool.execute.SRequest;

public interface pojo {

    @RequestType(SRequest.GET)
    @WebSpider("getName")
    String[] getName(@Param("count") String i);
}
