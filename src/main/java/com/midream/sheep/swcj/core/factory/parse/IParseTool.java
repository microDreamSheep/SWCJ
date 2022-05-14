package com.midream.sheep.swcj.core.factory.parse;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.w3c.dom.Node;

public interface IParseTool {
    /**
     * 解析文件
     * @param root swc的解析文件
     * */
    RootReptile parseRoot(Node root) throws ConfigException;
    /**
     * 解析配置文件
     * @param nodes 配置节点
     * @param config 配置文件
     * */
    void parseConfig(Node nodes, ReptileConfig config) throws ConfigException;
}
