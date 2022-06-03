package com.midream.sheep.swcj.core.executetool.execute.jsoup;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.util.function.StringUtil;
import com.midream.sheep.swcj.core.executetool.SWCJExecute;
import com.midream.sheep.swcj.core.executetool.execute.jsoup.pojo.Jsoup;
import com.midream.sheep.swcj.core.executetool.execute.jsoup.pojo.Pa;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.pojo.ExecuteValue;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author midreamsheep
 */
public class SWCJJsoup<T> implements SWCJExecute<T> {
    @Override
    @SuppressWarnings("all")
    public List execute(ExecuteValue executeValue, String... args) throws Exception {
        //获取节点对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        NodeList d = builder.parse(new InputSource(new StringReader(args[0].trim()))).getElementsByTagName("jsoup");
        Jsoup[] parse = Parse.parse(d);
        Document document = getConnection(executeValue);
        Map<String, List<String>> map = executeCorn(document, parse, executeValue.isHtml());
        if(executeValue.getClassNameReturn().equals(Constant.STRING_CLASS_NAME)){
            return map.get("string");
        }
        Class<?> aClass = Class.forName(executeValue.getClassNameReturn().replace("[]",""));
        List<Integer> list = new ArrayList<>();
        for (Jsoup jsoup : parse) {
            list.add(map.get(jsoup.getName()).size());
        }
        int max = Collections.min(Arrays.asList(list.toArray(new Integer[]{})));
        List listw = new ArrayList<>();

        for (int i = 0;i< max;i++) {
            Object o = aClass.getDeclaredConstructor().newInstance();
            for (Jsoup jsoup : parse) {
                String name = jsoup.getName();
                Method repay1 = aClass.getMethod("set" + StringUtil.StringToUpperCase(name), String.class);
                List<String> list1 = map.get(name);
                repay1.invoke(o, list1.size() > i ? map.get(name).get(i) : "");
            }
            listw.add(o);
        }
        return listw;
    }
    private Map<String,List<String>> executeCorn(Document document,Jsoup[] parse,boolean isHtml){
        Map<String,List<String>> values = new LinkedHashMap<>();
        for (Jsoup js : parse) {
            List<String> list = new ArrayList<>();
            values.put("".equals(js.getName()) ? "string" : js.getName(), list);
            Elements elements = null;
            for (int a = 0; a < js.getPas().length; a++) {
                Pa pa = js.getPas()[a];
                if (a == 0) {
                    elements = executePa(pa, document.select(pa.getValue()));
                } else {
                    Elements elements1 = new Elements();
                    for (Element element : elements) {
                        Elements pa1 = executePa(pa, element.select(pa.getValue()));
                        elements1.addAll(pa1);
                    }
                    elements = elements1;
                }
                if (a == js.getPas().length - 1) {
                    for (Element element : elements) {
                        String in = js.getPas()[js.getPas().length - 1].getElement();
                        if ("".equals(in)) {
                            if (isHtml) {
                                list.add(element.html());
                            } else {
                                list.add(element.text());
                            }
                        } else {
                            list.add(element.attr(in));
                        }
                    }
                }
            }
        }
        return values;
    }
    private Elements executePa(Pa p,Elements select){
        Elements elements = new Elements();
        for(int i = p.getStep();i<select.size();i+=(p.getAllstep()+1)){
            Element element = select.get(i);
            for (String s : p.getNot().split(",")) {
                if(!element.text().equals(s)){
                    elements.add(element);
                }
            }
        }
        return elements;
    }
    /**
     * @param executeValue necessary data
     * */
    private Document getConnection(ExecuteValue executeValue) throws ConfigException, IOException {
        Connection connection = org.jsoup.Jsoup.connect(executeValue.getUrl()).userAgent(executeValue.getUserAge()).
                cookies(executeValue.getValues()).ignoreContentType(true).data(executeValue.getValues()).
                timeout(Integer.parseInt(executeValue.getTimeout()));
        Document document;
        switch (executeValue.getType()){
            case GET:
                document = connection.get();
                break;
            case POST:
                document = connection.post();
                break;
            default:
                throw new ConfigException("不存在请求类型");
        }
        return document;
    }
    /**
     * @description Static inner classes parse XML
     * */
    public static class Parse {
        public static Jsoup[] parse(NodeList jsoup) throws ConfigException {
            //判断第一个子节点是否是jsoup
            String s = jsoup.item(0).getNodeName();
            if(s.equals("#text")){
                s = jsoup.item(1).getNodeName();
            }
            Jsoup[] jsoups;
            if("jsoup".equals(s)) {
                jsoups = parseJsoup(jsoup);
            }else if("pa".equals(s)){
                Jsoup jsoup1 = new Jsoup();
                jsoup1.setPas(parsePa(jsoup));
                jsoups = new Jsoup[]{jsoup1};
            }else {
                throw new ConfigException("你的配置文件有问题");
            }
            return jsoups;
        }
        private static Jsoup[] parseJsoup(NodeList jsoup){
            List<Jsoup> list = new ArrayList<>();
            for (int i = 0; i < jsoup.getLength(); i++) {
                Node item = jsoup.item(i);
                if(item.getNodeName().equals("#text")){
                    continue;
                }
                Jsoup js = new Jsoup();
                js.setName(item.getAttributes().getNamedItem("name").getNodeValue());
                js.setPas(parsePa(item.getChildNodes()));
                list.add(js);
            }
            return list.toArray(new Jsoup[]{});
        }
        private static Pa[] parsePa(NodeList pa){
            List<Pa> list = new ArrayList<>();
            for (int i = 1; i < pa.getLength(); i+=2) {
                Node item = pa.item(i);
                NamedNodeMap nodeMap = item.getAttributes();
                Pa p = new Pa();
                p.setAllstep(Integer.parseInt(nodeMap.getNamedItem("allStep").getNodeValue()));
                p.setNot(nodeMap.getNamedItem("not").getNodeValue());
                p.setStep(Integer.parseInt(nodeMap.getNamedItem("step").getNodeValue()));
                p.setElement(nodeMap.getNamedItem("element").getNodeValue());
                p.setValue(item.getTextContent().trim());
                list.add(p);
            }
            return list.toArray(new Pa[]{});
        }
    }
}
