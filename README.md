# SWSJ

## 什么是SWSJ

**SWSJ**是一个基于**jsoup和反射机制**的java**爬虫**框架，能够使你的爬虫与代码分离开，**降低解耦性**，同时

你的爬虫可以通过配置文件来配置，这意味这当你的某些需求更改时，能直接修改配置文件而不必去修改你的代码

## 他能干什么

它能使你的配置地狱更加地狱（呸

他能使你通过几个简单的配置来实现一个爬虫的具体实施

## 关于作者与不完善之处，求生欲极强

这只是一个14的少年的不成熟作品，我完成他仅用了一周，他可能有大量不完美之处，我正在不断的完善，目前基础功能已经完成，不知道如何修改，所以先发出来， [SWSJ爬虫框架: 一个通过配置文件实现爬虫的框架 (gitee.com)](https://gitee.com/midreamsheep/SWSJ)

jar包见仓库附件

## 使用详解

首先你需要一个配置文件，里面有部分不是必要的

然后通过xml工厂类(Xmlfactory)获取一个配置文件的所有实例

就可以通过工厂获取爬虫的实例，强转成接口就可以直接调用方法

### 一个返回字符串的简单具体实例

1.导入jar包（废话），暂不完善，并未上传maven

2.定义一个接口

```java
import com.midream.sheep.swsj.Annotation.WebSpider;

public interface test {
    @WebSpider("getHtml")//url的id,返回值与传参需要与配置文件一致
    String[] getData(int count);
    @WebSpider("getli")//支持多方法,非传参
    String[] getLi();
}
```

3.配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<SWCL>
    <config>
        <constructionSpace isAbsolute="false" workSpace="E:\临时文件"/>
        <timeout value="10000"/>
        <createTactics isCache="true"/>
    </config>
    <swc id="getHtml">
        <parentInterface class="com.midream.sheep.test"/>
        <url name="getHtml" inPutName="count">
            <type type="GET"/>
            <path path="https://pic.netbian.com/index_#{count}.html"/>
            <parseProgram  isHtml="true">
                <jsoup>
                    <pa>
                        #main>div.slist>ul.clearfix>li>a
                    </pa>
                </jsoup>
            </parseProgram>
        </url>
        <url name="getli" inPutName="">
            <type type="GET"/>
            <path path="https://pic.netbian.com/index_5.html"/>
            <parseProgram  isHtml="true">
                <jsoup>
                    <pa>
                        #main>div.slist>ul.clearfix>li
                    </pa>
                </jsoup>
            </parseProgram>
        </url>
    </swc>
</SWCL>
```

一个是有传参，一个是不传参，传参可传参改变值，参数数量可自定义，但是只能为基本数据类型（推荐String）

4.调方法

```java
XmlFactory xf = null;
        try {
            xf = new XmlFactory(XmlFactory.class.getClassLoader().getResource("").getPath()+"com/midream/sheep/test.xml");
            test getHtml = (test)xf.getWebSpider("getHtml");
            String[] li = getHtml.getLi();
            for (String s : li) {
                System.out.println(s);
            }
```

调XmlFactory获取配置，通过工厂获取类（注：需要强转）

5.直接通过接口调

### 一个返回javabean的复杂实例（较为详细）

#### 定义配置文件

首先我们需要目标：
https://www.qbiqu.com/modules/article/search.php?searchkey=%CD%F2%B9%C5
我们需要获取他的所有章节信息，所以需要封装一个章节实体类

```java
public class Page {
    private String title;
    private String writer;

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
```

然后配置xml配置文件

首先配置一个工作空间和缓冲策略

```xml
    <config>
        <constructionSpace isAbsolute="false" workSpace="E:\临时文件"/>
        <timeout value="10000"/>
        <userAgent>
            <value>Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62</value>
            <value>User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)</value>
            <value>User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)</value>
        </userAgent>
        <createTactics isCache="true"/>
    </config>
```

然后定义一个接口与方法并绑定到<swc>标签中

```java
public interface TestWeb {
    @WebSpider("getAllPages")
    Page[] getAllPages(String in);//返回值必须为数组
}
```

```xml
<swc id="test">
        <!--父类接口，爬虫通过接口调-->
        <parentInterface class="com.midream.demo.interfaces.TestWeb"/>
        <url name="getAllPages" inPutName="novelName">
            <type type="GET"/>
            <!--url链接-->
            <path path="https://www.qbiqu.com/modules/article/search.php?searchkey=#{novelName}"/>
        </url>
    </swc>
```

配置爬虫策略

```xml
            <parseProgram  isHtml="false">
<!--                <regular reg="href="/>-->
                <!--jsoup可以分为多层解析
                即一次<pa>就是一次解析
                not 排除固定文本
                -->
                <jsoup name="writer">
                    <pa not="" step="1" element="">
                        #nr>td.odd
                    </pa>
                </jsoup>
                <jsoup name="title">
                    <pa not="" step="0" element="">
                        #nr>td.odd>a
                    </pa>
                </jsoup>
                <jsoup name="url">
                    <pa not="" step="0" element="abs:href">
                        #nr>td.odd>a
                    </pa>
                </jsoup>
            </parseProgram>
```

整个xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<SWCJ>
    <config>
        <constructionSpace isAbsolute="false" workSpace="E:\临时文件"/>
        <timeout value="10000"/>
        <userAgent>
            <value>Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62</value>
            <value>User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)</value>
            <value>User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)</value>
        </userAgent>
        <createTactics isCache="true"/>
    </config>
    <swc id="test">
        <parentInterface class="com.midream.demo.interfaces.TestWeb"/>
        <url name="getAllPages" inPutName="novelName">
            <type type="GET"/>
            <path path="https://www.qbiqu.com/modules/article/search.php?searchkey=#{novelName}"/>
            <parseProgram  isHtml="false">
                <jsoup name="writer">
                    <pa not="" allStep="1" step="1" element="">
                        #nr>td.odd
                    </pa>
                </jsoup>
                <jsoup name="title">
                    <pa not="" step="0" element="">
                        #nr>td.odd>a
                    </pa>
                </jsoup>
                <jsoup name="url">
                    <pa not="" step="0" element="abs:href">
                        #nr>td.odd>a
                    </pa>
                </jsoup>
            </parseProgram>
        </url>
    </swc>
</SWCJ>
```

调用

````java
        XmlFactory xf = new XmlFactory("E:\\item\\webDemo\\out\\production\\webDemo\\test.xml");
        TestWeb test = (TestWeb) xf.getWebSpider("test");
        Page[] pages = test.getAllPages("%CD%F2%B9%C5");
````

暂不完善，欢迎提出bug

### xml配置模板

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<SWCJ>
    <!--全局配置-->
    <config>
        <!--工作空间，生成的字节码会存储到里面
        isAbsolute->是否是相对路径
        workplace->文件夹路径
        -->
        <constructionSpace isAbsolute="false" workSpace="E:\临时文件"/>
        <!--超时时间，请求超过这个时间报异常
        value->超时具体时间
        -->
        <timeout value="10000"/>
        <!--userAgrnt数据
        value->具体的userAgent文本
        -->
        <userAgent>
            <value>Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62</value>
            <value>User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)</value>
            <value>User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)</value>
        </userAgent>
        <!--爬虫策略
        cache缓存，将生成的类直接转为字节码加载
        非缓存，不会保存具体的对象
        -->
        <createTactics isCache="true"/>
    </config>
    <!--具体的某个爬虫类
         id 获取的标识
        -->
    <swc id="getHtml">
        <!--局部爬虫使用的cookies文本
        格式 键:值;···
        -->
        <cookies>
            uuid_tt_dd=4646545646-1642571061362-956268; UserName=xmdymcsheepsir;
        </cookies>
        <!--父类接口，爬虫通过接口调-->
        <parentInterface class="com.midream.sheep.WebTest"/>
        <!--请求配置
        一个配置对应一个方法
        name——>注解名
        inPutName 下文中使用的参数名
        -->
        <url name="getHtml" inPutName="">
            <!--请求类型
            当前仅支持POST和GET请求
            type="POST||GET"
            -->
            <type type="GET"/>
            <!--url链接-->
            <path path="https://pic.netbian.com/index_5.html"/>
            <!--解析html方案
            并不支持同时使用
            <regular>正则表达式 正则特殊值 ALL 即为返回所有文本（经过迭代不知道是否正常）
            <jsoup>jsoup配置-->
            <parseProgram  isHtml="false">
<!--                <regular reg="href="/>-->
                <!--jsoup可以分为多层解析
                一个jsoup就是一次解析
                即一次<pa>就是一次解析
                not 排除固定文本
                name 注入的属性
                -->
                <jsoup name="writer">
                    <!--pa可配置属性来选取目标Document
                    not 排除文本
                    step跳过的步长
                    element获取元素(加上abs:是绝对链接)
                    allStep每次跳过的步长
                    -->
                    <pa not="下一页" allStep="0" step="4" element="#{count}">
                        #main>div.slist>ul>li>a
                    </pa>
                </jsoup>
            </parseProgram>
        </url>
    </swc>
</SWCJ>
```

-------------------------------------------------------------------------------------

全篇完，欢迎大佬提出意见，目前优化思路见链接

