package com.example.mnstest.handler.mns;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: zhaomeinan
 * @Description: 队列使用测试类
 * @Date: Create in 17:40 2018/7/17
 * @Modificd By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MnsQueueHandlerTest {

    @Autowired
    private MnsQueueHandler mnsQueueHandler;

    @Test
    public void createQueue() throws Exception {
        QueueMeta meta = new QueueMeta();
        meta.setQueueName("Test-Queue-2018071703");
        CloudQueue cloudQueue = mnsQueueHandler.createQueue(meta);
        System.out.println(cloudQueue);
    }

    @Test
    public void deleteQueue() throws Exception {
        mnsQueueHandler.deleteQueue("Test-Queue-2018071701");
    }

    @Test
    public void producerOneMessage() throws Exception {
        String queueName = "Test-Queue-2018071701";
        Message message = new Message();
        message.setMessageBody("你好，这是一条队列消息");
        message = mnsQueueHandler.producerOneMessage(queueName,message);
        System.out.println(message);
    }

    @Test
    public void producerMessages() throws Exception {
        String queueName = "Test-Queue-2018071701";
        List<Message> messages = new ArrayList<Message>();
        Message message1 = new Message();
        message1.setMessageBody("你好，这是一条队列消息1");
        Message message2 = new Message();
        message2.setMessageBody("你好，这是一条队列消息2");
        Message message3 = new Message();
        message3.setMessageBody("你好，这是一条队列消息3");
        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages = mnsQueueHandler.producerMessages(queueName,messages);
        System.out.println(messages);
    }

    @Test
    public void consumer() throws Exception {
        mnsQueueHandler.consumer("cloud-queue-demo");
    }

}