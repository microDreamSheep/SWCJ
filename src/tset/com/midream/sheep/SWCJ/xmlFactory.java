package com.midream.sheep.SWCJ;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.util.classLoader.SWCJClassLoader;
import com.midream.sheep.SWCJ.util.xml.XmlFactory;
import com.midream.sheep.test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class xmlFactory {
    @Test
    public void xmlTest() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException {
        XmlFactory xf = new XmlFactory(XmlFactory.class.getClassLoader().getResource("test.xml").getPath());
        Object getHtml = xf.getWebSpider("getHtml");
        System.out.println(getHtml);

    }
    @Test
    public void modle() throws IOException {
        Object[] a = getdada();
        for (Object s : a) {
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
    private int timeout = 10000;
    private String[] userAgent = new String[]{"Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"};

    public Object[] getdada() {
        try {
            HashMap var1 = new HashMap();
            var1.put(" UserName", "xmdymcsheepsir");
            var1.put("uuid_tt_dd", "4646545646-1642571061362-956268");
            Document var2 = Jsoup.connect("https://www.ddyueshu.com/33_33907/").ignoreContentType(true).timeout(this.timeout).cookies(var1).userAgent(this.userAgent[(int)(Math.random() * (double)this.userAgent.length)]).get();
            ArrayList var3 = new ArrayList();
            Elements var4 = var2.select("#list");

            for(int var5 = 0; var5 < var4.size(); ++var5) {
                Object var6 = var4.get(var5);
                Elements var7 = ((Element)var6).select("dl>dd");

                for(int var8 = 0; var8 < var7.size(); ++var8) {
                    Object var9 = var7.get(var8);
                    var3.add(((Element)var9).html());
                }
            }

            String[] var10 = new String[var3.size()];

            for(int var11 = 0; var11 < var3.size(); ++var11) {
                var10[var11] = var3.get(var11).toString();
            }

            return var10;
        } catch (Exception var13) {
            var13.printStackTrace();
            return null;
        }
    }
    @Test
    public void jiazaiqi() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SWCJClassLoader swcjClassLoader = new SWCJClassLoader();
        Class<?> aClass = swcjClassLoader.loadData("com.midream.sheep.SWCJ.d0aec9bbe1ce4e4989bd9b8043c7a722", "E:\\workplae\\SWCJ\\target\\test-classes\\com\\midream\\sheep\\SWCJ\\d0aec9bbe1ce4e4989bd9b8043c7a722.class");
        test o = (test)aClass.getDeclaredConstructor().newInstance();

    }
}
