package test;

import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;
import com.midream.sheep.swcj.core.executetool.execute.jsoup.SWCJJsoup;

import java.util.*;

public class Config implements SWCJConfigClassConfiguration {
    @Override
    public boolean isCache() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 10000;
    }

    @Override
    public Collection<String> getUserAgents() {
        return Collections.singletonList("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36 Edg/97.0.1072.62");
    }

    @Override
    public String getWorkplace() {
        return "";
    }

    @Override
    public Map<String, String> getExecuteConfigurationClass() {
        Map<String,String> map = new HashMap<>();
        map.put("jsoup", SWCJJsoup.class.getName());
        return map;
    }
}
