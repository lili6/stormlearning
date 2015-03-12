package com.lili.zk.queues;

import org.apache.curator.framework.recipes.queue.QueueSerializer;

/**
 * Created by lili on 15/3/12.
 */
public class QueueItemSerializer implements QueueSerializer<String>{

    @Override
    public byte[] serialize(String item) {
        return item.getBytes();
    }

    @Override
    public String deserialize(byte[] bytes) {
        return new String(new String(bytes));
    }
}
