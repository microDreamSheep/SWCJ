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
import java.util.*;

public class xmlFactory {
    @Test
    public void xmlTest() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        XmlFactory xf = new XmlFactory(XmlFactory.class.getClassLoader().getResource("test.xml").getPath());
        test getHtml = (test)xf.getWebSpider("getHtml");
        for(int i = 2;i<7;i++){
            String[] getdada = getHtml.getdada(i);
            for (String s : getdada) {
                System.out.println(s);
            }
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
    private int timeout = 10000;
    private String[] userAgent = new String[]{"Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"};
        @Test
        public void getdada(){
            try{
                org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://www.17k.com/chapter/3377666/45200781.html").ignoreContentType(true).timeout(timeout)
                        .userAgent(userAgent[(int) (Math.random()*userAgent.length)]).get();
                java.util.List<String> list = new java.util.ArrayList<>();
                org.jsoup.select.Elements select = document.select("div.area>div.read");
                System.out.println(document.html());
                for (int i = 0;i<select.size();i++) {
                    org.jsoup.nodes.Element element = select.get(i);org.jsoup.select.Elements element1 = element.select("div.readArea>div.readAreaBox content");
                    for(int c4528ec06670945e8a9c836561426389d = 0;c4528ec06670945e8a9c836561426389d<element1.size();c4528ec06670945e8a9c836561426389d++) {
                        org.jsoup.nodes.Element element3 = element1.get(c4528ec06670945e8a9c836561426389d);org.jsoup.select.Elements element2 = element3.select("div.p>p");
                        for(int c6a3815f893cc4a6691157e94343eefa4 = 0;c6a3815f893cc4a6691157e94343eefa4<element2.size();c6a3815f893cc4a6691157e94343eefa4++) {
                            org.jsoup.nodes.Element element4 = element2.get(c6a3815f893cc4a6691157e94343eefa4);list.add(element4.html());
                        }
                    }
                }
                String[] result = list.toArray(new String[]{});
                for (String s : result) {
                    System.out.println(s);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    @Test
    public void jiazaiqi() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SWCJClassLoader swcjClassLoader = new SWCJClassLoader();
        Class<?> aClass = swcjClassLoader.loadData("com.midream.sheep.SWCJ.d0aec9bbe1ce4e4989bd9b8043c7a722", "E:\\workplae\\SWCJ\\target\\test-classes\\com\\midream\\sheep\\SWCJ\\d0aec9bbe1ce4e4989bd9b8043c7a722.class");
        test o = (test) aClass.getDeclaredConstructor().newInstance();

    }
}
