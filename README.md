# SWCJ爬虫框架

#### 介绍
一个通过配置文件实现爬虫的框架,目标是让你的配置地狱更地狱，呸，让你通过配置文件实现爬虫，哪怕你是非程序员
，后面我们会推出爬虫GUI工具（甚至是手机app)
目前尚处于不完善阶段，jsoup仅支持输出，不支持动态加载，如果你知道这个异常如何解决(见最下方)或希望练手，欢迎加入我们

#### 软件架构
暂无


##### 困扰我们的异常：
```java
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
