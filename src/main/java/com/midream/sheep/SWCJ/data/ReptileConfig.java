package com.midream.sheep.SWCJ.data;

import java.util.ArrayList;
import java.util.List;

public class ReptileConfig {
    private String workplace = ReptileConfig.class.getClassLoader().getResource("")+ "com/midream/sheep/SWCJ/wordkplace";
    private int timeout = 10000;
    private List<String> userAgents = new ArrayList<>();
    private boolean isCache = true;//是否缓存
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
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
