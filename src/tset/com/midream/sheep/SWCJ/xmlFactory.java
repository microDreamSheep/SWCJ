package com.midream.sheep.SWCJ;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.util.classLoader.SWCJClassLoader;
import com.midream.sheep.SWCJ.util.xml.XmlFactory;
import com.midream.sheep.test;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class xmlFactory {
    @Test
    public void xmlTest() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        XmlFactory xf = new XmlFactory(XmlFactory.class.getClassLoader().getResource("test.xml").getPath());
        test getHtml = (test)xf.getWebSpider("getHtml");
            String[] getdada = getHtml.getdada("a");
            for (String s : getdada) {
                System.out.println(s);
            }

    }
    @Test
    public void First(){
        System.out.println(true+"");
    }

    @Test
    public void getHtml(){
        try{java.util.Map<String,String> map = new java.util.HashMap<>();map.put(" UserName","xmdymcsheepsir");
            map.put("uuid_tt_dd","4646545646-1642571061362-956268");

            org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://www.ddyueshu.com/33_33907/").ignoreContentType(true).timeout(10000)
                    .cookies(map).get();
            java.util.List<String> list = new java.util.LinkedList<>();
            org.jsoup.select.Elements select = document.select("#list");
            for (org.jsoup.nodes.Element element : select) {
                org.jsoup.select.Elements element1 = element.select("dl>dd");
                for (org.jsoup.nodes.Element element2 : element1) {list.add(element2.html());
                }
            }
            String[] result = list.toArray(new String[]{});
            for (String s : result) {
                System.out.println(s);
            }
        }catch (Exception e)
        {e.printStackTrace();}
    }
    @Test
    public void jiazaiqi() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SWCJClassLoader swcjClassLoader = new SWCJClassLoader();
        Class<?> aClass = swcjClassLoader.loadData("com.midream.sheep.SWCJ.d0aec9bbe1ce4e4989bd9b8043c7a722", "E:\\workplae\\SWCJ\\target\\test-classes\\com\\midream\\sheep\\SWCJ\\d0aec9bbe1ce4e4989bd9b8043c7a722.class");
        test o = (test) aClass.getDeclaredConstructor().newInstance();

    }
}
