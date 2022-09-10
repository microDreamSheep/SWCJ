

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class test2 {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, IOException {
        ExecutorService execute = new ThreadPoolExecutor(1,10,
                5, TimeUnit.SECONDS,
                new SynchronousQueue<>()
                , Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
        for(int i = 0;i<=10000;i++){
            int finalI = i;
            execute.execute(()->{
                System.out.println(Thread.currentThread().getName()+"正在执行"+ finalI);
            });
        }
    }
}
