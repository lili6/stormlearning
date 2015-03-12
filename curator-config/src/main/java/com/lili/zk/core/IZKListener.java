package com.lili.zk.core;

import org.apache.curator.framework.CuratorFramework;

/**
 * Created by lili on 15/3/12.
 */
public interface IZKListener {
    void executor(CuratorFramework client);
}
