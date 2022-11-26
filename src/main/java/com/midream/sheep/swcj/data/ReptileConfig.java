package com.midream.sheep.swcj.data;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.core.APIClassInter.ExecuteConfigurationClass;
import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;
import com.midream.sheep.swcj.pojo.enums.ChooseStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author midreamSheep
 * 全局配置
 * */
public class ReptileConfig {
    //工作空间
    private String workplace = ReptileConfig.class.getClassLoader().getResource("")+ "com/midream/sheep/swcj/wordkplace";
    //超时时间
    private int timeout = 10000;
    //方法分析策略
    private ChooseStrategy choice;
    //浏览器模拟
    private List<String> userAgents = new ArrayList<>();

    private boolean isCache = true;//是否缓存
    /**
     * 增加浏览器标识
     * */
    public void addUserAgent(String userAgent){
        userAgents.add(userAgent);
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public void setUserAgents(List<String> userAgents) {
        this.userAgents = userAgents;
    }

    public ReptileConfig() {
        userAgents.add("Mozilla/5.0 (X11; Linux x86_64; rv:96.0) Gecko/20100101 Firefox/96.0");
    }

    public List<String> getUserAgents() {
        return userAgents;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) throws ConfigException {
        this.workplace = workplace.replace("file:/","").replace("file:\\","");
        File place = new File(workplace);
        //生成文件夹
        if(!place.exists()){
            place.mkdirs();
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public ChooseStrategy getChoice() {
        if(choice==null){
            return ChooseStrategy.ANNOTATION;
        }
        return choice;
    }

    public void setChoice(ChooseStrategy choice) {
        this.choice = choice;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void parse(SWCJConfigClassConfiguration swcjConfigClassConfiguration){
        this.isCache = swcjConfigClassConfiguration.isCache();
        this.choice = ChooseStrategy.METHOD_NAME;
        this.timeout = swcjConfigClassConfiguration.getTimeout();
        this.userAgents.addAll(swcjConfigClassConfiguration.getUserAgents());
        this.workplace = swcjConfigClassConfiguration.getWorkplace();
        Map<String, String> executeConfigurationClass = swcjConfigClassConfiguration.getExecuteConfigurationClass();
        if(executeConfigurationClass!=null){
            Constant.PutExecutesMap(executeConfigurationClass);
        }
    }

    @Override
    public String toString() {
        return "ReptileConfig{" +
                "workplace='" + workplace + '\'' +
                ", timeout=" + timeout +
                ", choice=" + choice +
                ", userAgents=" + userAgents +
                ", isCache=" + isCache +
                '}';
    }
}
