import com.midream.sheep.swcj.core.analyzer.CornAnalyzer;
import com.midream.sheep.swcj.core.analyzer.IAnalyzer;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import test.image;
import test.pojo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class test2 {
    private static final String in = "#[in][swcj;]#[fx][swcj;]#[isHtml][swcj;]#[type][swcj;]#[url][swcj;]#[userage][swcj;]#[cookies][swcj;]#[values][swcj;]#[timeout][swcj;]#[class][swcj;]#[method]";
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        SWCJClassLoader swcjClassLoader = new SWCJClassLoader();
        Class<?> data = swcjClassLoader.loadData("a2b0ea666ad0248cb97aa074e8b5f3bbb", "E://a.class");
        Object o = data.getDeclaredConstructor().newInstance();
        pojo a =  (pojo)o;
        //please use Latin characters Offsets:
        System.out.println("开始调用");
        image[] it = a.getIt("5", "5");
        System.out.println("调用完毕");

        for (image image : it) {
            System.out.println(image);
        }
    }
}
