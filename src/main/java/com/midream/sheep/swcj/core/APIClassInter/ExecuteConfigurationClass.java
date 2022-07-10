package com.midream.sheep.swcj.core.APIClassInter;

import java.util.Map;
/**
 * executes配置文件，注入一组执行层配置
 * execute标签中的executeConfig标签中使用全限类名
 * */
public interface ExecuteConfigurationClass {
    /**
     * 获取执行层map
     * @return 执行层map key为执行层名称，value为执行层全限类名
     * */
    Map<String,String> getExecuteConfiguration();
}
