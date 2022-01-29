import com.midream.a67296d7ac55248039415fda7a20cec0a;
import com.midream.sheep.Image;
import com.midream.sheep.WEBSPI;
import com.midream.sheep.WebTest;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.util.xml.XmlFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author midreamsheep
 */
public class daaddsa {
    @Test
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        XmlFactory xmlFactory = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath() + "test.xml");
        WebTest getHtml = (WebTest)xmlFactory.getWebSpider("getHtml");
        Image[] a = getHtml.a("6");
        for (Image image : a) {
            System.out.println(image.toString());
        }
    }
}
