package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.data.Constant;
import com.midream.sheep.SWCJ.test;
import com.midream.sheep.SWCJ.util.io.SIO;
import com.midream.sheep.SWCJ.util.javaassist.Assist;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.jsoup.Jsoup;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlFactoryTest {

    //xmlFactory构造器测试
    @Test
    public void constructorTest() throws Exception {
        XmlFactory xmlFactory = new XmlFactory(SIO.class.getClassLoader().getResource("test.xml").getFile());
        Object o = xmlFactory.getWebSpider("getHtml");
        System.out.println(o instanceof test);
        test test = (test)o;
        String[] webcladjwda = test.webcladjwda();
        for (String s : webcladjwda) {
            System.out.println(s);
        }
    }
    @Test
    public void assistTest() throws CannotCompileException, IOException {
        //获取类池
        ClassPool classPool = new Assist().getClassPool();
        //获取随机名，防止重复
        String name = UUID.randomUUID().toString().replace("-","");
        CtClass ctClass = classPool.makeClass("com.midream.sheep"
                +"."+ name
        );
        CtField field = CtField.make("private String data = \"12312\";", ctClass);
        ctClass.addField(field);
        ctClass.writeFile("E:/down");
    }
    @Test
    public void model() throws Exception{
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString().replace("-",""));
    }
    private int timeout = 10000;
    private String[] userAgent = new String[]{"Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)", "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"};
    @Test
    public void adwd() throws IOException {
        String[] webcladjwda = webcladjwda();
        for (String s : webcladjwda) {
            System.out.println(s);
        }
    }
    public String[] webcladjwda() throws IOException {
        HashMap var1 = new HashMap();
        var1.put(" UserName", "xmdymcsheepsir");
        var1.put("uuid_tt_dd", "4646545646-1642571061362-956268");
        String var2 = Jsoup.connect("https://www.ddyueshu.com/33_33907/").ignoreContentType(true).timeout(this.timeout).cookies(var1).userAgent(this.userAgent[(int)(Math.random() * (double)this.userAgent.length)]).get().html();
        Pattern var3 = Pattern.compile("^[0-9]*$");
        Matcher var4 = var3.matcher(var2);
        var4.find();
        int i = var4.groupCount();
        String[] var5 = new String[i];
        for(int d = 0;d<i;d++){
            var5[d] = var4.group(d);
        }
        return var5;
    }
    @Test
    public void regtest(){
        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(\\D*)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }
}
