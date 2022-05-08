import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import org.xml.sax.SAXException;
import test.POJOTEST;
import test.image;
import test.pojo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author midreamsheep
 */
public class Test {
    @org.junit.Test
    public void test() throws ConfigException, EmptyMatchMethodException, InterfaceIllegal, ClassNotFoundException, InterruptedException {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory();
        swcjXmlFactory.addResource(Test.class.getClassLoader().getResource("").getPath() + "/test.xml");
        Thread.sleep(2000);
        long start = System.currentTimeMillis();
        pojo html = (pojo) swcjXmlFactory.getWebSpider("getHtml");
        long end = System.currentTimeMillis();
        System.out.println("获取类消耗了"+(end-start)+"ms");
        image[] it = html.getIt(5);
        for (image image : it) {
            System.out.println(image.toString());
        }
    }

    @org.junit.Test
    public void sda() {
    }
}
