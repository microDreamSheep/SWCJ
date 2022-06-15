import com.midream.sheep.swcj.core.analyzer.CornAnalyzer;
import com.midream.sheep.swcj.core.analyzer.IAnalyzer;
import com.midream.sheep.swcj.pojo.image;

import java.util.ArrayList;
import java.util.List;

public class test2 {
    private static final String in = "1:5:#{count}[swcj;]com.midream.sheep.swcj.pojo.image[swcj;]false[swcj;]GET[swcj;]https://pic.netbian.com/index_5.html[swcj;]User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)[swcj;][swcj;][swcj;]10000[swcj;]com.midream.sheep.swcj.core.executetool.execute.jsoup.SWCJJsoup[swcj;]"+"                <Jsoup>\n" +
            "                    <jsoup name=\"name\">\n" +
            "                        <pa not=\"下一页\" allStep=\"0\" step=\"0\" element=\"\">\n" +
            "                            #main>div.slist>ul>li>a\n" +
            "                        </pa>\n" +
            "                    </jsoup>\n" +
            "                    <jsoup name=\"url\">\n" +
            "                        <pa not=\"下一页\" allStep=\"0\" step=\"0\" element=\"\">\n" +
            "                            #main>div.slist>ul\n" +
            "                        </pa>\n" +
            "                        <pa not=\"下一页\" allStep=\"0\" step=\"0\" element=\"abs:href\">\n" +
            "                            li>a\n" +
            "                        </pa>\n" +
            "                    </jsoup>\n" +
            "                </Jsoup>".replace("\n","");
    public static void main(String[] args) {
        IAnalyzer<image> i = new CornAnalyzer<>();
        image[] images = i.execute(in, "5").toArray(new image[0]);
        for (image image : images) {
            System.out.println(image);
        }
    }

}
