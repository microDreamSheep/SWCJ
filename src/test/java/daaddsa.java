import com.midream.sheep.WebTest;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.util.xml.XmlFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author midreamsheep
 */
public class daaddsa {
    @Test
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        XmlFactory xf = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath() + "/test.xml");
        WebTest getHtml = (WebTest) xf.getWebSpider("getHtml");
        String[] htmls = getHtml.a("https://pic.netbian.com/index_5.html");
        for (String s : htmls) {
            System.out.println(s);
        }

    }

    @Test
    public void adw() throws IOException {
        Document document = Jsoup.connect("").get();
    }


}
