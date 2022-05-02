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
import java.lang.reflect.InvocationTargetException;

/**
 * @author midreamsheep
 */
public class Test {
    @org.junit.Test
    public void test() throws ConfigException, IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InterfaceIllegal, ClassNotFoundException {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory(Test.class.getClassLoader().getResource("").getPath() + "/test.xml");
        long start = System.currentTimeMillis();
        pojo html = (pojo) swcjXmlFactory.getWebSpider("getHtml");
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        image[] it = html.getIt();
        for (image image : it) {
            System.out.println(image.toString());
        }

    }

    @org.junit.Test
    public void ts() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    }

}
