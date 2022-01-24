# SWCJ爬虫框架

#### 介绍
一个通过配置文件实现爬虫的框架,目标是让你的配置地狱更地狱，呸，让你通过配置文件实现爬虫，哪怕你是非程序员
，后面我们会推出爬虫GUI工具（甚至是手机app)
目前尚处于不完善阶段，支持动态加载，如果你知道如何优化(见最下方)或希望练手，欢迎加入我们

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
        <createTactics isCache="false"/>
    </config>
    <!--具体的某个爬虫类
         inPutType:传入的数值类型（可以为空）
         id 调用的方法名
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
        -->
        <url>
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
            <parseProgram  isHtml="true">
<!--                <regular reg="href="/>-->
                <!--jsoup可以分为多层解析
                即一次<pa>就是一次解析
                -->
                <jsoup>
                    <!--;pa可配置属性来选取目标Document-->
                    <pa>
                        #readArea>div>div.p
                    </pa>
                    <pa>
                        p
                    </pa>
                </jsoup>
            </parseProgram>
        </url>
        <!--返回值类型
        基本数据类型直接用，
        引用类型必须全类名如:java.lang.String
        -->
        <returnType type="Object[]"/>
    </swc>
</SWCL>
```
##### 困扰我们的异常：
目前我们使用的是生成类到本地在编译加载
assist使用过程中产生奇妙的异常，所以被放弃了
```
java.lang.VerifyError: Bad type on operand stack
Exception Details:
  Location:
    com/midream/sheep/d0aec9bbe1ce4e4989bd9b8043c7a722.getdada()[Ljava/lang/Object; @125: invokevirtual
  Reason:
    Type 'java/lang/Object' (current frame, stack[0]) is not assignable to 'org/jsoup/nodes/Element'
  Current Frame:
    bci: @125
    flags: { }
    locals: { 'com/midream/sheep/d0aec9bbe1ce4e4989bd9b8043c7a722', 'java/util/HashMap', 'org/jsoup/nodes/Document', 'java/util/ArrayList', 'org/jsoup/select/Elements', integer, 'java/lang/Object' }
    stack: { 'java/lang/Object', 'java/lang/String' }
  Bytecode:
    0000000: bb00 1059 b700 144c 2b12 1612 18b9 001e
    0000010: 0300 572b 1220 1222 b900 1e03 0057 1224
    0000020: b800 2a04 b900 3002 002a b400 32b9 0035
    0000030: 0200 2bb9 0039 0200 2ab4 003b b800 412a
    0000040: b400 3bbe 876b 8e32 b900 4302 00b9 0047
    0000050: 0100 4dbb 0049 59b7 004a 4e2c 124c b600
    0000060: 523a 0403 3605 1505 1904 b600 56a2 0043
    0000070: 1904 1505 b600 593a 0619 0612 5bb6 0052
    0000080: 3a07 0336 0815 0819 07b6 0056 a200 1e19
    0000090: 0715 08b6 0059 3a09 2d19 09b6 005f b900
    00000a0: 6502 0057 8408 01a7 ffde 8405 01a7 ffb9
    00000b0: 2db9 0068 0100 3a0a 190a b03a 0b19 0bb6
    00000c0: 006f a700 0301 b0                      
  Exception Handler Table:
    bci [0, 187] => handler: 187
  Stackmap Table:
    full_frame(@102,{Object[#2],Object[#16],Object[#114],Object[#73],Object[#116],Integer},{})
    append_frame(@133,Object[#4],Object[#116],Integer)
    same_frame(@170)
    chop_frame(@176,3)
    full_frame(@187,{Object[#2]},{Object[#106]})
    full_frame(@197,{Object[#2],Top,Top,Top,Top,Top,Top,Top,Top,Top,Top,Object[#106]},{})
 
 
    at java.base/java.lang.Class.getDeclaredConstructors0(Native Method)
    at java.base/java.lang.Class.privateGetDeclaredConstructors(Class.java:3305)
    at java.base/java.lang.Class.getConstructor0(Class.java:3510)
    at java.base/java.lang.Class.getDeclaredConstructor(Class.java:2691)
    at com.midream.sheep.SWCJ.util.xml.ReptilesBuilder.reptilesBuilder(ReptilesBuilder.java:161)
    at com.midream.sheep.SWCJ.util.xml.XmlFactory.getWebSpider(XmlFactory.java:175)
    at com.midream.sheep.SWCJ.xmlFactory.xmlTest(xmlFactory.java:21)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:78)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:567)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
    at org.junit.runners.BlockJUnit4ClassRunner$1.evaluate(BlockJUnit4ClassRunner.java:100)
    at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:366)
    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:103)
    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:63)
    at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
    at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
    at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
    at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
    at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
    at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
    at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
    at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
    at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:69)
    at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
    at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:221)
    at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)
```
