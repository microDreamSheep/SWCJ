package com.midream;

public class a67296d7ac55248039415fda7a20cec0a implements com.midream.sheep.WebTest {
    private static int timeout = 10000;
    private static String[] userAgent = new String[]{"Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"};

    public com.midream.sheep.Image[] a(String args0) {
        try {
            org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://pic.netbian.com/index_6.html").ignoreContentType(true).timeout(timeout)
                    .userAgent(userAgent[(int) (Math.random() * userAgent.length)]).get();
            java.util.List<String> title = new java.util.LinkedList<>();
            java.util.List<String> url = new java.util.LinkedList<>();
            {
                java.util.List<String> SWCJlist = new java.util.ArrayList<>();
                boolean owdad = true;
                int SWCJasd = 0;
                org.jsoup.select.Elements select = document.select("#main>div.slist>div.clearfix>li>a>b");
                for (int i = 0; i < select.size(); i++) {
                    SWCJasd++;
                    org.jsoup.nodes.Element element = select.get(i);
                    if (1 == 1) {
                        SWCJlist.add(element.html());
                    }
                }
                title = SWCJlist;
            }
            {
                java.util.List<String> SWCJlist = new java.util.ArrayList<>();
                boolean owdad = true;
                int SWCJasd = 0;
                org.jsoup.select.Elements select = document.select("#main>div.slist>div.clearfix>li>a");
                for (int i = 0; i < select.size(); i++) {
                    SWCJasd++;
                    org.jsoup.nodes.Element element = select.get(i);
                    if (1 == 1) {
                        SWCJlist.add(element.attr("abs:href"));
                    }
                }
                url = SWCJlist;
            }
            int[] casdsad = {title.size(), url.size(),};
            java.util.Arrays.sort(casdsad);
            int maxawdwa = casdsad[casdsad.length - 1];
            java.util.List<com.midream.sheep.Image> lists = new java.util.LinkedList<>();
            for (int i = 0; i < maxawdwa; i++) {
                Class<?> aClass = Class.forName("com.midream.sheep.Image");
                Object o = aClass.getDeclaredConstructor().newInstance();
                try {
                    aClass.getDeclaredMethod("setTitle", String.class).invoke(o, title.get(i));
                } catch (Exception e) {
                }
                try {
                    aClass.getDeclaredMethod("setUrl", String.class).invoke(o, url.get(i));
                } catch (Exception e) {
                }
                lists.add((com.midream.sheep.Image) o);
            }
            return lists.toArray(new com.midream.sheep.Image[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
