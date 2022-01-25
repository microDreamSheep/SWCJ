package com.midream.sheep;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.util.xml.XmlFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author midreamsheep
 */
public class dawdsad {
    @Test
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        XmlFactory xf = new XmlFactory(XmlFactory.class.getClassLoader().getResource("").getPath()+"//"+"test.xml");
        TestWeb html = (TestWeb)xf.getWebSpider("getHtml");

            String[] getdata = html.getdata();
            for (String s : getdata) {
                System.out.println(s);
            }

    }
}
