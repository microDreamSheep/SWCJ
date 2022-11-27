package test;

import com.midream.sheep.swcj.annotation.Param;
import com.midream.sheep.swcj.annotation.RequestType;
import com.midream.sheep.swcj.annotation.config.SWCJMetaConfig;
import com.midream.sheep.swcj.annotation.config.method.*;
import com.midream.sheep.swcj.core.executetool.execute.SRequest;

@SWCJMetaConfig(id="getHtml",className = Config.class)
public interface pojoAnnotation {
    @RequestType(SRequest.GET)
    @ExecuteLogic(processProgram = "<Jsoup><jsoup name=\"\"> <pa not=\"下一页\" allStep=\"0\" step=\"0\" element=\"\">#main>div.slist>ul>li>a</pa></jsoup></Jsoup>",
            executorKey = "jsoup",IsHtml = false)
    @UrlConfig("https://pic.netbian.com/index_#{count}.html")
    String[] test(@Param("count") String count);
}
