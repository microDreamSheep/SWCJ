import com.midream.sheep.WebTest;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.util.xml.XmlFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        XmlFactory xf = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath() + "/test.xml");
        WebTest getHtml = (WebTest) xf.getWebSpider("getHtml");
        String[] htmls = getHtml.a("https://pic.netbian.com/index_5.html");
        System.out.println(htmls.length);

    }

    @Test
    public void adw() throws IOException {
        Document document = Jsoup.connect("").get();
    }

    public String[] a(String count) {
        try {
            java.util.Map<String, String> map = new java.util.HashMap<>();
            map.put(" UserName", "xmdymcsheepsir");
            map.put("uuid_tt_dd", "4646545646-1642571061362-956268");

            org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("#{count}").ignoreContentType(true)
                    .cookies(map).get();
            java.util.List<String> list = new java.util.ArrayList<>();
            boolean owdad = true;
            org.jsoup.select.Elements select = document.select("#main>div.slist>ul");
            for (int i = 0; i < select.size(); i++) {
                org.jsoup.nodes.Element element = select.get(i);
                if (owdad) {
                    i += 5;
                    owdad = false;
                }
                boolean asd = true;
                org.jsoup.select.Elements elementi1 = element.select("li>a");
                for (int ce5e8fe0ac83044fdbdca5856cc8ccbf1 = 0; ce5e8fe0ac83044fdbdca5856cc8ccbf1 < elementi1.size(); ce5e8fe0ac83044fdbdca5856cc8ccbf1++) {
                    if (asd) {
                        ce5e8fe0ac83044fdbdca5856cc8ccbf1 += 5;
                        asd = false;
                    }
                    org.jsoup.nodes.Element element3 = elementi1.get(ce5e8fe0ac83044fdbdca5856cc8ccbf1);
                    list.add(element3.text());
                }
            }
            String[] result = list.toArray(new String[]{});
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
