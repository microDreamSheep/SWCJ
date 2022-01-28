import com.midream.sheep.WEBSPI;
import com.midream.sheep.WebTest;
import com.midream.sheep.pojo.Novel;
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
        XmlFactory xf = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath() + "/test.xml");
        WEBSPI getHtml = (WEBSPI) xf.getWebSpider("awds");
        String[] hrefs = getHtml.a("");
        for (String href : hrefs) {
            System.out.println(href);
        }

    }

    @Test
    public void adw() throws ClassNotFoundException {

    }


}
