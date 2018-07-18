package com.example.mnstest.handler.mns;

import com.aliyun.mns.client.CloudPullTopic;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Vector;

/**
 * @Author: zhaomeinan
 * @Description: mns Topic使用
 *
 * 主题操作具体接口参考：https://help.aliyun.com/document_detail/27431.html?spm=a2c4g.11186623.6.587.vbkV55
 *
 * @Date: Create in 18:05 2018/7/16
 * @Modificd By:
 */
@ConditionalOnBean({MnsHandler.class})
@Component
public class MnsTopicHandler {

  private final Logger logger = LoggerFactory.getLogger(MnsTopicHandler.class);

  @Autowired
  private MNSClient client;

  /**
   * @Author: zhaomeinan
   * @Description: 创建主题
   * @Date: 18:27 2018/7/16
   * @Modificd By:
   * @Param: [meta]
   * @return: com.aliyun.mns.client.CloudTopic
   * @throw: 请描述异常信息
   */
  public CloudTopic createTopic(String topicName) {
    if (StringUtils.isEmpty(topicName)) {
      return null;
    }
    TopicMeta meta = new TopicMeta();
    meta.setTopicName(topicName);
    CloudTopic cloudTopic = client.createTopic(meta);
    logger.info("MNS - 创建主题成功；主题名称: {}", topicName);
    return cloudTopic;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 创建主题, 同时指定订阅关系 <p>
   *
   *  参考：https://help.aliyun.com/document_detail/34483.html?spm=a2c4g.11186623.6.639.N37RC0
   *
   * @Date: 16:47 2018/7/17
   * @Modificd By:
   * @Param: [topicName, consumerNameList, queueMetaTemplate]
   * @return: com.aliyun.mns.client.CloudPullTopic
   * @throw: 请描述异常信息
   */
  public CloudPullTopic createPullTopic(String topicName, Vector<String> consumerNameList,
      QueueMeta queueMetaTemplate) {
    TopicMeta topicMeta = new TopicMeta();
    topicMeta.setTopicName(topicName);
    CloudPullTopic cloudPullTopic = client
        .createPullTopic(topicMeta, consumerNameList, true, queueMetaTemplate);
    logger.info("MNS - 创建主题成功，同时指定订阅关系；主题名称: {}", topicName);
    return cloudPullTopic;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 删除主题
   * @Date: 18:29 2018/7/16
   * @Modificd By:
   * @Param: [topicName]
   * @return: void
   * @throw: 请描述异常信息
   */
  public void deleteTopic(String topicName) {
    if (StringUtils.isEmpty(topicName)) {
      return;
    }
    CloudTopic topic = client.getTopicRef(topicName);
    topic.delete();
    logger.info("MNS - 删除主题成功；主题名称: {}", topicName);
  }

  /**
   * @Author: zhaomeinan
   * @Description: 创建订阅，指定由谁来消费发送到topic的消息(推送类型:http)
   *
   * 可以订阅 Topic 中带有特定标签的消息。在创建订阅时指定消息过滤标签， 然后
   * PublshMessage 时指定消息标签，MNS在推送消息时会根据标签进行过滤， 仅推送消息标签与订阅中指定的过滤标签匹配的消息到指定 Endpoint 上。
   *
   * @Date: 13:35 2018/7/17
   * @Modificd By:
   * @Param: [topicName, subscriptionName, filterTag, endpoint]
   * @return: java.lang.String
   * @throw: 请描述异常信息
   */
  public String subscribeWithPushTypeHttp(String topicName, String subscriptionName,
      String filterTag, String endpoint) {
    if (StringUtils.isEmpty(topicName) || StringUtils.isEmpty(subscriptionName) || StringUtils
        .isEmpty(endpoint)) {
      return null;
    }

    CloudTopic topic = client.getTopicRef(topicName);

    SubscriptionMeta subMeta = new SubscriptionMeta();
    subMeta.setSubscriptionName(subscriptionName);
    subMeta.setEndpoint(endpoint);
    subMeta.setNotifyContentFormat(SubscriptionMeta.NotifyContentFormat.JSON);

    if (!StringUtils.isEmpty(filterTag)) {
      subMeta.setFilterTag(filterTag); //设置订阅的filterTag
    }

    String url = topic.subscribe(subMeta);

    logger.info("MNS - 创建订阅成功；主题名称: {}；订阅名称：{}；http：{}", topicName, subscriptionName, endpoint);

    return url;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 创建订阅，指定由谁来消费发送到topic的消息(推送类型:队列)
   *
   * 可以订阅 Topic 中带有特定标签的消息。在创建订阅时指定消息过滤标签， 然后
   * PublshMessage 时指定消息标签，MNS在推送消息时会根据标签进行过滤， 仅推送消息标签与订阅中指定的过滤标签匹配的消息到指定 Endpoint 上。
   *
   * @Date: 13:58 2018/7/17
   * @Modificd By:
   * @Param: [topicName, subscriptionName, filterTag, queueName]
   * @return: java.lang.String
   * @throw: 请描述异常信息
   */
  public String subscribeWithPushTypeQueue(String topicName, String subscriptionName,
      String filterTag, String queueName) {
    if (StringUtils.isEmpty(topicName) || StringUtils.isEmpty(subscriptionName) || StringUtils
        .isEmpty(queueName)) {
      return null;
    }

    CloudTopic topic = client.getTopicRef(topicName);
    SubscriptionMeta subMeta = new SubscriptionMeta();
    subMeta.setSubscriptionName(subscriptionName);
    subMeta.setNotifyContentFormat(SubscriptionMeta.NotifyContentFormat.SIMPLIFIED);
    subMeta.setEndpoint(topic.generateQueueEndpoint(queueName));

    if (!StringUtils.isEmpty(filterTag)) {
      subMeta.setFilterTag(filterTag);
    }

    String url = topic.subscribe(subMeta);

    logger.info("MNS - 创建订阅成功；主题名称: {}；订阅名称：{}；queue：{}", topicName, subscriptionName, queueName);

    return url;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 取消订阅
   * @Date: 13:40 2018/7/17
   * @Modificd By:
   * @Param: [topicName, subscriptionName]
   * @return: void
   * @throw: 请描述异常信息
   */
  public void unsubscribe(String topicName, String subscriptionName) {
    if (StringUtils.isEmpty(topicName) || StringUtils.isEmpty(subscriptionName)) {
      return;
    }
    CloudTopic topic = client.getTopicRef(topicName);

    logger.info("MNS - 取消订阅成功；主题名称: {}；订阅名称：{}", topicName, subscriptionName);

    topic.unsubscribe(subscriptionName);
  }

  /**
   * @Author: zhaomeinan
   * @Description: 发布消息
   *
   * 消息发布到 Topic 后，最长存活时间是 1天，过期后消息会变成 Expired 状态，将被垃圾回收器回收。
   *
   * @Date: 13:44 2018/7/17
   * @Modificd By:
   * @Param: [topicName, messageBody, filterTag]
   * @return: com.aliyun.mns.model.TopicMessage
   * @throw: 请描述异常信息
   */
  public TopicMessage publishMessage(String topicName, String messageBody, String filterTag) {
    if (StringUtils.isEmpty(topicName) || StringUtils.isEmpty(messageBody)) {
      return null;
    }
    CloudTopic topic = client.getTopicRef(topicName);
    TopicMessage msg = new Base64TopicMessage(); //可以使用TopicMessage结构，选择不进行Base64加密
    msg.setMessageBody(messageBody);
    if (!StringUtils.isEmpty(filterTag)) {
      msg.setMessageTag(filterTag); //设置该条发布消息的filterTag
    }
    TopicMessage topicMessage = topic.publishMessage(msg);

    logger.info("MNS - 发布消息成功；主题名称: {}", topicName);

    return topicMessage;
  }
}
