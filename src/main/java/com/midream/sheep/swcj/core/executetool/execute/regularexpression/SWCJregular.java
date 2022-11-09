package com.midream.sheep.swcj.core.executetool.execute.regularexpression;

import com.midream.sheep.api.clazz.ClazzBuilder;
import com.midream.sheep.api.http.HTTPTool;
import com.midream.sheep.swcj.core.executetool.SWCJExecute;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.XmlSpecialStrings;
import com.midream.sheep.swcj.pojo.ExecuteValue;
import com.midream.sheep.swcj.util.function.StringUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author midreamsheep
 */
public class SWCJregular<T> implements SWCJExecute<T> {

    @Override
    public List<T> execute(ExecuteValue executeValue, String... args) throws Exception {
        for (Map.Entry<String, String> entry : XmlSpecialStrings.map.entrySet()) {
            executeValue.setUrl(executeValue.getUrl().replace(entry.getKey(), entry.getValue()));
        }
        String text = getText(executeValue);
        Map<String, List<String>> values = new LinkedHashMap<>();
        //获取节点对象
        NodeList d = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(
                        new InputSource(new StringReader(args[0].trim()))
                )
                .getElementsByTagName(RegConstants.regTag)
                .item(0)
                .getChildNodes();
        for (int i = 0; i < d.getLength(); i++) {
            Node item = d.item(i);
            if (!item.getNodeName().equals(RegConstants.regTag)) {
                continue;
            }
            //放入属性map
            List<String> list = new LinkedList<>();
            putMap(item.getAttributes().getNamedItem(RegConstants.nameAttribute),values,list);
            //放入正则表达式
            String trim = item.getTextContent().trim();
            for (Map.Entry<String, String> entry : XmlSpecialStrings.map.entrySet()) {
                trim = trim.replace(entry.getKey(), entry.getValue());
            }
            String del = item.getAttributes().getNamedItem("del").getTextContent();
            String[] nots = new String[0];
            if (del != null && !del.equals("")) {
                String[] split = del.trim().split("';");
                nots = new String[split.length];
                for (int a = 0; a < split.length; a++) {
                    nots[a] = split[a].substring(1);
                }
            }
            Matcher matcher = Pattern.compile(trim).matcher(text);
            while (matcher.find()) {
                String value = matcher.group();
                for (String notStr : nots) {
                    value = value.replace(notStr, "");
                }
                list.add(value);
            }
        }
        return values.size()==1?(List<T>) values.get(RegConstants.strName):buildClasses(executeValue,values);
    }
    private List<T> buildClasses(ExecuteValue executeValue,Map<String,List<String>> fields){
        ClazzBuilder clazzBuilder = new ClazzBuilder();
        clazzBuilder.setClass(executeValue.getClassNameReturn().replace("[]", Constant.nullString));
        return (List<T>) clazzBuilder.buildByMap(fields);
    }
    private void putMap(Node name,Map<String,List<String>> values,List<String> list){
        if (name != null && !name.getNodeValue().trim().equals(Constant.nullString)) {
            values.put(name.getNodeValue(), list);
        } else {
            values.put(RegConstants.strName, list);
        }
    }
    /**
     * 获取字符串
     */
    private String getText(ExecuteValue executeValue) {
        try {
            return StringUtil.getStringByStream(new BufferedReader(new InputStreamReader(HTTPTool.getHttpURLConnection(executeValue).getInputStream(), "GBK")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
