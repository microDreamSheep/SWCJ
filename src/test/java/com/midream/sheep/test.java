package com.midream.sheep;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.SWCJXmlFactory;
import com.midream.sheep.swcj.core.xmlfactory.CoreXmlFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author midreamsheep
 */
public class test {
    @Test
    public void test() throws ConfigException, IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InterfaceIllegal {
        SWCJXmlFactory swcjXmlFactory = new CoreXmlFactory(test.class.getClassLoader().getResource("").getPath()+"/test.xml");
        pojo html = (pojo)swcjXmlFactory.getWebSpider("getHtml");
        String[] it = html.getIt();
        for (String s : it) {
            System.out.println(s);
        }
    }
}
