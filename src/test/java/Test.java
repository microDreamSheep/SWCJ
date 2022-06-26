
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.parse.bystr.BetterXmlParseTool;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.ThreadXmlFactory;
import org.xml.sax.SAXException;
import test.image;
import test.pojo;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author midreamsheep
 */
public class Test {
    public static void main(String[] args) throws ConfigException, IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InterfaceIllegal {
        SWCJXmlFactory swcjXmlFactory = new ThreadXmlFactory();
        swcjXmlFactory.setParseTool(new BetterXmlParseTool());
        long start = System.currentTimeMillis();
        swcjXmlFactory.parse(new File(Objects.requireNonNull(Test.class.getClassLoader().getResource("")).getPath() + "/Efficient.xml"));
        long end = System.currentTimeMillis();
        System.out.println((end-start)+"ms");
        pojo html = (pojo) swcjXmlFactory.getWebSpiderById("getHtml");
        image[] it = html.getit("5","5");
        for (image image : it) {
            System.out.println(image);
        }
        swcjXmlFactory.invokeSpecialMethod();
    }
}
