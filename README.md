# SWCJ爬虫框架

#### 介绍
一个通过配置文件实现爬虫的框架,目标是让你的配置地狱更地狱，呸，让你通过配置文件实现爬虫，哪怕你是非程序员
，后面我们会推出爬虫GUI工具（甚至是手机app)
目前尚处于不完善阶段，支持动态加载，如果你知道如何优化或希望练手，欢迎加入我们

#### 软件架构
暂无
### 配置模板
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<SWCL>
    <!--全局配置-->
    <config>
        <!--工作空间，生成的字节码会存储到里面
        isAbsolute->是否是相对路径
        workplace->文件夹路径
        -->
        <constructionSpace isAbsolute="true" workSpace="com/midream/sheep"/>
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
        非缓存，将生成的类输出到本地class在加载
        -->
        <createTactics isCache="true"/>
    </config>
    <!--具体的某个爬虫类
         inPutType:传入的数值类型（可以为空）
         id 获取的方式
        下面使用传入值时使用的标识符（可以为空）-->
    <swc id="getHtml" inPutType="" inPutName="">
        <!--局部爬虫使用的cookies文本
        格式 键:值;···
        -->
        <cookies>
            uuid_tt_dd=4646545646-1642571061362-956268; UserName=xmdymcsheepsir;
        </cookies>
        <!--父类接口，爬虫通过接口调-->
        <parentInterface class="com.midream.sheep.test"/>
        <!--请求配置
        一个配置对应一个方法
        -->
        <url name="getHtml">
            <!--请求类型
            当前仅支持POST和GET请求
            type="POST||GET"
            -->
            <type type="GET"/>
            <!--url链接-->
            <url path="https://www.17k.com/chapter/3377666/45200781.html"/>
            <!--解析html方案
            并不支持同时使用
            <regular>正则表达式 正则特殊值 ALL 即为返回所有文本
            <jsoup>jsoup配置-->
            <parseProgram  isHtml="false">
<!--                <regular reg="href="/>-->
                <!--jsoup可以分为多层解析
                即一次<pa>就是一次解析
                -->
                <jsoup>
                    <!--;pa可配置属性来选取目标Document-->
                    <pa>
                        div.area>div.read
                    </pa>
                    <pa>
                        div.readArea>div.readAreaBox
                    </pa>
                    <pa>
                        div.p>p
                    </pa>
                </jsoup>
            </parseProgram>
            <!--返回值类型
            基本数据类型直接用，
            引用类型必须全类名如:java.lang.String
            -->
            <returnType type="String[]"/>
        </url>
    </swc>
</SWCL>
```