
import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.core.analyzer.CornAnalyzer;
import com.midream.sheep.swcj.core.analyzer.IAnalyzer;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import sun.misc.Unsafe;
import test.image;
import test.pojo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class test2 {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IOException {
        long start = System.currentTimeMillis();
        Class<pojo> aClass = pojo.class;
        InputStream is = aClass.getResourceAsStream("pojo.class");
        byte[] datas = new byte[is.available()];
        is.read(datas);
        is.close();
        long end = System.currentTimeMillis();
        System.out.println("读取pojo.class耗时：" + (end - start));
    }
}
