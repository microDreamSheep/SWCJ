package com.midream.sheep.SWCJ;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.util.xml.XmlFactory;
import com.midream.sheep.test;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class xmlFactory {
    @Test
    public void xmlTest() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException {
        XmlFactory xf = new XmlFactory(XmlFactory.class.getClassLoader().getResource("test.xml").getPath());
        test getHtml = (test)xf.getWebSpider("getHtml");
        String[] html = getHtml.getHtml();
        System.out.println(html.length);
    }
    @Test
    public void modle() throws IOException {
        //提供一个list接收数据
        List<String> result = new LinkedList<>();

        org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://www.ddyueshu.com/33_33907/").get();
        //第一层
        org.jsoup.select.Elements select = document.select("#list");
        for (org.jsoup.nodes.Element element : select) {
            //第二层
            org.jsoup.select.Elements select1 = element.select("dl>dd");
            for (org.jsoup.nodes.Element element1 : select1) {
                //最后一层
                result.add(element1.html());
            }
        }
        String[] strings = result.toArray(new String[]{});
        for (String s : strings) {
            System.out.println(s);
        }
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

}
