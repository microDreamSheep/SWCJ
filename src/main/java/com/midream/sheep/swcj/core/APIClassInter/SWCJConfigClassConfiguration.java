package com.midream.sheep.swcj.core.APIClassInter;

import java.util.Collection;
import java.util.Map;

public interface SWCJConfigClassConfiguration {
    boolean isCache();

    int getTimeout();

    Collection<String> getUserAgents();

    String getWorkplace();

    Map<String, String> getExecuteConfigurationClass();
}
