package com.midream.sheep.SWCJ.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * @author midreamSheep
 * 全局配置
 * */
public class ReptileConfig {
    //工作空间
    private String workplace = ReptileConfig.class.getClassLoader().getResource("")+ "com/midream/sheep/SWCJ/wordkplace";
    //超时时间
    private int timeout = 10000;
    //浏览器模拟
    private List<String> userAgents = new ArrayList<>();
    private boolean isCache = true;//是否缓存
    /**
     * 增加浏览器
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

    @Override
    public String toString() {
        return "ReptileConfig{" +
                "workplace='" + workplace + '\'' +
                ", timeout=" + timeout +
                ", userAgents=" + userAgents +
                '}';
    }

    public List<String> getUserAgents() {
        return userAgents;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
        File place = new File(workplace);
        //生成文件夹
        if(!place.exists()){
            place.mkdirs();
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
