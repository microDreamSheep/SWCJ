
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.effecient.EffecientCompiler;
import com.midream.sheep.swcj.core.factory.annotationfactory.CoreFactoryByClass;
import com.midream.sheep.swcj.core.factory.annotationfactory.SWCJClassFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.SWCJXmlFactory;
import org.xml.sax.SAXException;
import test.pojo;
import test.pojoAnnotation;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author midreamsheep
 */
public class Test {
    public static void main(String[] args) throws ConfigException, IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InterfaceIllegal {
        /*SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory();
        long start1 = System.currentTimeMillis();
        swcjXmlFactory.parse(new File(Objects.requireNonNull(Test.class.getClassLoader().getResource("")).getPath() + "/test.xml"));
        long end1 = System.currentTimeMillis();
        System.out.println("解析"+(end1 - start1));

        long start2 = System.currentTimeMillis();
        swcjXmlFactory.setCompiler(new EffecientCompiler());
        pojo html = (pojo) swcjXmlFactory.getWebSpiderById("downloader");
        long end2 = System.currentTimeMillis();
        System.out.println("构建"+(end2 - start2));
        String[] it = html.gethtml("9");
        for (String s : it) {
            System.out.println(s);
        }
        swcjXmlFactory.invokeSpecialMethod();*/
        SWCJClassFactory swcjClassFactory = new CoreFactoryByClass();
        swcjClassFactory.setCompiler(new EffecientCompiler());
        long start3 = System.currentTimeMillis();
        pojoAnnotation parse = swcjClassFactory.parse(pojoAnnotation.class);
        long end3 = System.currentTimeMillis();
        System.out.println("解析"+(end3 - start3));
        for (String s : parse.test("9")) {
            System.out.println(s);
        }
    }
}
