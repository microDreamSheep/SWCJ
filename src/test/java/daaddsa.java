import com.midream.sheep.WebTest;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.util.xml.XmlFactory;
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
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException , ConfigException, InterfaceIllegal {
        XmlFactory xf = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath()+"/test.xml");
        WebTest getHtml = (WebTest)xf.getWebSpider("getHtml");
        String[] test = getHtml.ad();
        for (String s : test) {
            System.out.println(s);
        }
    }
    @Test
    public void testada(){
        System.out.println(int.class.getName());
        System.out.println(long.class.getName());
        System.out.println(double.class.getName());
        System.out.println(short.class.getName());
        System.out.println(int.class.getName());
        System.out.println(int.class.getName());

    }
}
