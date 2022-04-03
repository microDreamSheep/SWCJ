import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.SWCJXmlFactory;
import com.midream.sheep.swcj.core.xmlfactory.CoreXmlFactory;
import org.xml.sax.SAXException;
import test.image;
import test.pojo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author midreamsheep
 */
public class Test {
    @org.junit.Test
    public void test() throws ConfigException, IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InterfaceIllegal {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory(Test.class.getClassLoader().getResource("").getPath() + "/test.xml");
        pojo html = (pojo)swcjXmlFactory.getWebSpider("getHtml");
        image[] it = html.getIt();
        image[] var4 = it;
        int var5 = it.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            image image = var4[var6];
            System.out.println(image.toString());
        }

    }
}
