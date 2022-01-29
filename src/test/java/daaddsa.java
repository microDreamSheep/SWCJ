import com.midream.sheep.WEBSPI;
import com.midream.sheep.WebTest;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.util.xml.XmlFactory;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author midreamsheep
 */
public class daaddsa {
    @Test
    public void test() throws IOException, ParserConfigurationException, SAXException, EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        XmlFactory xf = new XmlFactory(daaddsa.class.getClassLoader().getResource("").getPath() + "/test.xml");
        WebTest getHtml2 = (WebTest) xf.getWebSpider("getHtml");
        String[] a = getHtml2.a("abs:href");
        System.out.println(a.length);
        for (String s : a) {
            System.out.println(s);
        }
    }

    @Test
    public void adw() {
        a("href");
    }

    public String[] a(String count) {
        try {
            java.util.Map<String, String> A8754ef03d9f2469db68b833a372c2ea0 = new java.util.HashMap<>();
            A8754ef03d9f2469db68b833a372c2ea0.put(" UserName", "xmdymcsheepsir");
            A8754ef03d9f2469db68b833a372c2ea0.put("uuid_tt_dd", "4646545646-1642571061362-956268");

            org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://pic.netbian.com/index_5.html").ignoreContentType(true)
                    .cookies(A8754ef03d9f2469db68b833a372c2ea0).get();
            String[] SWCJresult = null;
            {
                int Welementi2 = 0;
                java.util.List<String> SWCJlist = new java.util.ArrayList<>();
                boolean owdad = true;
                int SWCJasd = 0;
                org.jsoup.select.Elements select = document.select("#main>div.slist>ul");
                for (int i = 0; i < select.size(); i++) {
                    SWCJasd++;
                    org.jsoup.nodes.Element element = select.get(i);
                    if (1 == 1 && !element.text().equals("下一页")) {
                        boolean Belementi1 = true;
                        org.jsoup.select.Elements elementi1 = element.select("li");
                        for (int c078305406ccf4489bf9d7893bfd3ce28 = 0; c078305406ccf4489bf9d7893bfd3ce28 < elementi1.size(); c078305406ccf4489bf9d7893bfd3ce28++) {
                            org.jsoup.nodes.Element element3 = elementi1.get(c078305406ccf4489bf9d7893bfd3ce28);
                            if (1 == 1 && !element3.text().equals("下一页")) {
                                boolean Belementi2 = true;
                                org.jsoup.select.Elements elementi2 = element3.select("a");
                                for (int cb4d3cbb6f6d4423e90b78ccdee06ef3b = 0; cb4d3cbb6f6d4423e90b78ccdee06ef3b < elementi2.size(); cb4d3cbb6f6d4423e90b78ccdee06ef3b++) {
                                    Welementi2++;
                                    org.jsoup.nodes.Element element4 = elementi2.get(cb4d3cbb6f6d4423e90b78ccdee06ef3b);
                                    if (1 == 1 && !element4.text().equals("下一页")) {
                                        SWCJlist.add(element4.text());
                                    }
                                }
                            }
                        }
                    }
                }
                SWCJresult = SWCJlist.toArray(new String[]{});
            }
            return SWCJresult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
