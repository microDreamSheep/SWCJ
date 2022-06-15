import com.midream.sheep.swcj.core.analyzer.CornAnalyzer;
import com.midream.sheep.swcj.core.analyzer.IAnalyzer;
import test.image;

public class test2 {
    private static final String in = "#[in][swcj;]#[fx][swcj;]#[isHtml][swcj;]#[type][swcj;]#[url][swcj;]#[userage][swcj;]#[cookies][swcj;]#[values][swcj;]#[timeout][swcj;]#[class][swcj;]#[method]";
    public static void main(String[] args) {
        IAnalyzer<image> i = new CornAnalyzer<>();
        image[] images = i.execute(in, "2").toArray(new image[0]);
        for (image image : images) {
            System.out.println(image);
        }
    }
}
