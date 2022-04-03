package com.midream.sheep;

public class a27c270b8e4c64c19ba95e4b92ddaf2c9 implements com.midream.sheep.pojo {
    private static int timeout = 10000;
    private static String[] userAgent = new String[]{"Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"};
    ;

    public com.midream.sheep.image[] getIt() {
        try {
            java.util.Map<String, String> Ae4595e5d31194382b5c56273179a8c30 = new java.util.HashMap<>();
            Ae4595e5d31194382b5c56273179a8c30.put(" UserName", "xmdymcsheepsir");
            Ae4595e5d31194382b5c56273179a8c30.put("uuid_tt_dd", "4646545646-1642571061362-956268");

            org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://pic.netbian.com/index_5.html").ignoreContentType(true).timeout(timeout)
                    .cookies(Ae4595e5d31194382b5c56273179a8c30).userAgent(userAgent[(int) (Math.random() * userAgent.length)]).get();
            java.util.List<String> name = new java.util.LinkedList<>();
            java.util.List<String> url = new java.util.LinkedList<>();
            {
                java.util.List<String> SWCJlist = new java.util.ArrayList<>();
                boolean owdad = true;
                int SWCJasd = 0;
                org.jsoup.select.Elements select = document.select("#main>div.slist>ul>li>a");
                for (int i = 0; i < select.size(); i++) {
                    SWCJasd++;
                    if ((SWCJasd - 1) % 1 != 0) {
                        continue;
                    }
                    org.jsoup.nodes.Element element = select.get(i);
                    if (1 == 1 && !element.text().equals("下一页")) {
                        SWCJlist.add(element.text());
                    }
                }
                name = SWCJlist;
            }
            {
                int Welementi1 = 0;
                java.util.List<String> SWCJlist = new java.util.ArrayList<>();
                boolean owdad = true;
                int SWCJasd = 0;
                org.jsoup.select.Elements select = document.select("#main>div.slist>ul");
                for (int i = 0; i < select.size(); i++) {
                    SWCJasd++;
                    if ((SWCJasd - 1) % 1 != 0) {
                        continue;
                    }
                    org.jsoup.nodes.Element element = select.get(i);
                    if (1 == 1 && !element.text().equals("下一页")) {
                        boolean Belementi1 = true;
                        org.jsoup.select.Elements elementi1 = element.select("li>a");
                        for (int c00f0a1cb77494fa5be9f20cebede78c7 = 0; c00f0a1cb77494fa5be9f20cebede78c7 < elementi1.size(); c00f0a1cb77494fa5be9f20cebede78c7++) {
                            if (Belementi1) {
                                c00f0a1cb77494fa5be9f20cebede78c7 += 5;
                                if (c00f0a1cb77494fa5be9f20cebede78c7 >= elementi1.size()) {
                                    break;
                                }
                            }
                            Welementi1++;
                            if ((Welementi1 - 1) % 1 != 0) {
                                continue;
                            }
                            org.jsoup.nodes.Element element3 = elementi1.get(c00f0a1cb77494fa5be9f20cebede78c7);
                            if (1 == 1 && !element3.text().equals("下一页")) {
                                SWCJlist.add(element3.attr("abs:href"));
                            }
                        }
                    }
                }
                url = SWCJlist;
            }
            int[] SWCJvars1 = {name.size(), url.size(),};
            java.util.Arrays.sort(SWCJvars1);
            int maxawdwa = SWCJvars1[SWCJvars1.length - 1];
            java.util.List<com.midream.sheep.image> lists = new java.util.LinkedList<>();
            for (int i = 0; i < maxawdwa; i++) {
                Class<?> aClass = Class.forName("com.midream.sheep.image");
                Object o = aClass.getDeclaredConstructor().newInstance();
                try {
                    aClass.getDeclaredMethod("setName", String.class).invoke(o, name.get(i));
                } catch (Exception e) {
                }
                try {
                    aClass.getDeclaredMethod("setUrl", String.class).invoke(o, url.get(i));
                } catch (Exception e) {
                }
                lists.add((com.midream.sheep.image) o);
            }
            return lists.toArray(new com.midream.sheep.image[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
