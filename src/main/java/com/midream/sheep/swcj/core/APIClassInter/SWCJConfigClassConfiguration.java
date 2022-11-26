package com.midream.sheep.swcj.core.APIClassInter;

import java.util.Collection;

public interface SWCJConfigClassConfiguration {
    boolean isCache();

    int getTimeout();

    Collection<String> getUserAgents();

    String getWorkplace();

    ExecuteConfigurationClass getExecuteConfigurationClass();
}
