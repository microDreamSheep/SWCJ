package com.midream.sheep.swcj.core.executetool.execute.regularexpression;

import com.midream.sheep.swcj.core.executetool.SWCJExecute;
import com.midream.sheep.swcj.pojo.ExecuteValue;
import com.midream.sheep.swcj.util.function.StringUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author midreamsheep
 */
public class SWCJregular implements SWCJExecute {
    int max = 0;
    @Override
    @SuppressWarnings("all")
    public List execute(ExecuteValue executeValue, String... args) throws Exception {
        String text = getText(executeValue);
        Map<String,List<String>> values = new LinkedHashMap<>();
        //获取节点对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        NodeList d = builder.parse(new InputSource(new StringReader(args[0].trim()))).getElementsByTagName("reg").item(0).getChildNodes();
        for(int i = 0;i<d.getLength();i++){
            Node item = d.item(i);
            if(item.getNodeName().equals("reg")){
                List<String> list = new LinkedList<>();
                Node name = item.getAttributes().getNamedItem("name");
                if(name!=null&&!name.getNodeValue().trim().equals("")){
                    values.put(name.getNodeValue(),list);
                }else {
                    values.put("str",list);
                }
                String trim = item.getTextContent().replace("&gt;",">").replace("&lt;","<").trim();
                Pattern r = Pattern.compile(trim);
                Matcher matcher = r.matcher(text);
                while (matcher.find()){
                    list.add(matcher.group());
                }
                if(list.size()>max){
                    max = list.size();
                }
            }
        }
        if(values.size()==1){
            return  values.get("str");
        }
        Class<?> aClass = Class.forName(executeValue.getClassNameReturn().replace("[]",""));
        List listw = new LinkedList();
        for(int i = 0;i<max;i++){
            listw.add(aClass.getDeclaredConstructor().newInstance());
        }
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            for(int i = 0;i<entry.getValue().size();i++){
                Object o = listw.get(i);
                Method repay1 = aClass.getMethod("set" + StringUtil.StringToUpperCase(entry.getKey()), String.class);
                repay1.invoke(o,entry.getValue().get(i));
            }
        }
        return listw;
    }
    /**
     * 获取字符串
     * */
    private String getText(ExecuteValue executeValue){
        HttpURLConnection con;

        BufferedReader buffer;
        StringBuilder resultBuffer;
        InputStream inputStream = null;
        try {
            URL url = new URL(executeValue.getUrl());
            //得到连接对象
            con = (HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod(executeValue.getType().getValue());
            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "text/html;charset=GBK");
            con.setRequestProperty("cookie",executeValue.getCookies());
            con.setRequestProperty("user-agent",executeValue.getUserAge());

            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);
            //得到响应码
            int responseCode = con.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                inputStream = con.getInputStream();
                //将响应流转换成字符串
                resultBuffer = new StringBuilder();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream,"GBK"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                return resultBuffer.toString();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
