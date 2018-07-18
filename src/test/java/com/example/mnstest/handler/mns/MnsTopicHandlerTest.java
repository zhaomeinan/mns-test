package com.example.mnstest.handler.mns;

import com.aliyun.mns.client.CloudPullTopic;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.model.QueueMeta;
import com.aliyun.mns.model.TopicMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Vector;

import static org.junit.Assert.*;

/**
 * @Author: zhaomeinan
 * @Description: 主题使用测试类
 * @Date: Create in 18:05 2018/7/17
 * @Modificd By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MnsTopicHandlerTest {

    @Autowired
    private MnsTopicHandler mnsTopicHandler;

    @Test
    public void createTopic() throws Exception {
        CloudTopic cloudTopic = mnsTopicHandler.createTopic("Test-Topic-2017071701");
        System.out.println(cloudTopic);
    }

    @Test
    public void createPullTopic() throws Exception {
        String topicName = "Test-Topic-2017071702";
        Vector<String> consumerNameList = new Vector<String>();
        String consumerName1 = "consumer001";
        String consumerName2 = "consumer002";
        String consumerName3 = "consumer003";
        consumerNameList.add(consumerName1);
        consumerNameList.add(consumerName2);
        consumerNameList.add(consumerName3);
        QueueMeta queueMetaTemplate = new QueueMeta();
        queueMetaTemplate.setPollingWaitSeconds(30);
        CloudPullTopic cloudPullTopic = mnsTopicHandler.createPullTopic(topicName, consumerNameList, queueMetaTemplate);
        System.out.println(cloudPullTopic);
    }

    @Test
    public void deleteTopic() throws Exception {
        mnsTopicHandler.deleteTopic("Test-Topic-2017071701");
    }

    @Test
    public void subscribeWithPushTypeHttp() throws Exception {
        String topicName = "Test-Topic-2017071701";
        String subscriptionName = "sub01";
        String filterTag = "";
        String endpoint = "http://zhaomeinan.w3.luyouxia.net/mns-called";
        String str = mnsTopicHandler.subscribeWithPushTypeHttp(topicName, subscriptionName, filterTag, endpoint);
        System.out.println(str);
        //https://1783322637466422.mns.cn-hangzhou-switch.aliyuncs.com/topics/Test-Topic-2017071701/subscriptions/sub01
    }

    @Test
    public void subscribeWithPushTypeQueue() throws Exception {
        String topicName = "Test-Topic-2017071701";
        String subscriptionName = "sub03Topic";
        String filterTag = "";
        String queueName = "cloud-queue-demo";
        String str = mnsTopicHandler.subscribeWithPushTypeQueue(topicName, subscriptionName, filterTag, queueName);
        System.out.println(str);
    }

    @Test
    public void unsubscribe() throws Exception {
        String topicName = "Test-Topic-2017071701";
        String subscriptionName = "sub02Topic";
        mnsTopicHandler.unsubscribe(topicName, subscriptionName);
    }

    @Test
    public void publishMessage() throws Exception {
        String topicName = "Test-Topic-2017071701";
        String messageBody = "这是一条消息，来自topic,测试queue接收消息22";
        String filterTag = "";
        TopicMessage topicMessage = mnsTopicHandler.publishMessage(topicName, messageBody, filterTag);
        System.out.println(topicMessage);
    }

}