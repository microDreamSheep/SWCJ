import com.midream.sheep.WebTest;
import com.midream.sheep.pojo.Novel;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.util.xml.XmlFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author midreamsheep
 */
public class daaddsa {
    @Test
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        XmlFactory xf = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath() + "/test.xml");
        WebTest getHtml = (WebTest) xf.getWebSpider("getHtml");
        Novel[] a = getHtml.a("https://pic.netbian.com/index_5.html");
        System.out.println(a.length);
        for (Novel novel : a) {
            System.out.println(novel.toString());
        }
    }

    @Test
    public void adw() throws ClassNotFoundException {
        a("");
    }

    public com.midream.sheep.pojo.Novel[] a(String count) {
        try {
            java.util.Map<String, String> map = new java.util.HashMap<>();
            map.put(" UserName", "xmdymcsheepsir");
            map.put("uuid_tt_dd", "4646545646-1642571061362-956268");

            org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://pic.netbian.com/index_5.html").ignoreContentType(true)
                    .cookies(map).get();
            java.util.List<String> title = new java.util.LinkedList<>();
            java.util.List<String> writer = new java.util.LinkedList<>();
            {
                java.util.List<String> list = new java.util.ArrayList<>();
                boolean owdad = true;
                org.jsoup.select.Elements select = document.select("#main>div.slist>ul");
                for (int i = 0; i < select.size(); i++) {
                    org.jsoup.nodes.Element element = select.get(i);
                    boolean asd = true;
                    org.jsoup.select.Elements elementi1 = element.select("li>a");
                    for (int c11ebe39ddd0a40a489daf25a70ed1eaf = 0; c11ebe39ddd0a40a489daf25a70ed1eaf < elementi1.size(); c11ebe39ddd0a40a489daf25a70ed1eaf++) {
                        org.jsoup.nodes.Element element3 = elementi1.get(c11ebe39ddd0a40a489daf25a70ed1eaf);
                        list.add(element3.attr("abs:href"));
                    }
                }
                title = list;
            }
            {
                java.util.List<String> list = new java.util.ArrayList<>();
                boolean owdad = true;
                org.jsoup.select.Elements select = document.select("#main>div.slist>ul");
                for (int i = 0; i < select.size(); i++) {
                    org.jsoup.nodes.Element element = select.get(i);
                    boolean asd = true;
                    org.jsoup.select.Elements elementi1 = element.select("li>a");
                    for (int c275bb82f241642b2b2cb6711d9c0b3f2 = 0; c275bb82f241642b2b2cb6711d9c0b3f2 < elementi1.size(); c275bb82f241642b2b2cb6711d9c0b3f2++) {
                        org.jsoup.nodes.Element element3 = elementi1.get(c275bb82f241642b2b2cb6711d9c0b3f2);
                        list.add(element3.attr("abs:href"));
                    }
                }
                writer = list;
            }
            int[] casdsad = {title.size(), writer.size(),};
            java.util.Arrays.sort(casdsad);
            int maxawdwa = casdsad[casdsad.length - 1];
            java.util.List<com.midream.sheep.pojo.Novel> lists = new java.util.LinkedList<>();
            System.out.println(maxawdwa);
            for (int i = 0; i < maxawdwa; i++) {
                Class<?> aClass = Class.forName("com.midream.sheep.pojo.Novel");
                Object o = aClass.getDeclaredConstructor().newInstance();
                aClass.getDeclaredMethod("setTitle", String.class).invoke(o, title.get(i));
                aClass.getDeclaredMethod("setWriter", String.class).invoke(o, writer.get(i));
                lists.add((com.midream.sheep.pojo.Novel) o);
                return lists.toArray(new com.midream.sheep.pojo.Novel[]{});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
