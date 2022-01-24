package com.midream.sheep;
public class ac9ec70708c6c4d60b3db40b17eed4568{
    private static int timeout = 10000;
private static String[] userAgent = new String[]{"Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62","User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)","User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"};

public static Object[] getdada(){
try{java.util.Map<String,String> map = new java.util.HashMap<>();map.put(" UserName","xmdymcsheepsir");
map.put("uuid_tt_dd","4646545646-1642571061362-956268");

org.jsoup.nodes.Document document = org.jsoup.Jsoup.connect("https://www.ddyueshu.com/33_33907/").ignoreContentType(true).timeout(timeout)
.cookies(map).userAgent(userAgent[(int) (Math.random()*userAgent.length)]).get();
java.util.List/*<String>*/ list = new java.util.ArrayList/*<>*/();
org.jsoup.select.Elements select = document.select("#list");
for (int i = 0;i<select.size();i++) {
org.jsoup.nodes.Element element = select.get(i);org.jsoup.select.Elements element1 = element.select("dl>dd");
for(int a = 0;a<element1.size();a++) {
org.jsoup.nodes.Element element2 = element1.get(a);list.add(element2.html());
}
}
Object[] result = list.toArray();
return result;
}catch (Exception e){
e.printStackTrace();
}
return null;
}

    public static void main(String[] args) {
        getdada();
    }
}