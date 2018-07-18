package com.example.mnstest.handler.mns;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhaomeinan
 * @Description: mns队列使用
 *
 * 队列是消息存储的目的地，队列可以分成普通队列和延时队列两类。
 * 消息包含数据和属性，可以分成普通消息和延时消息，在一个队列中能唯一标识一个消息的有MessageId 和 ReceiptHandle 两种。
 * 队列解释具体参考：https://help.aliyun.com/document_detail/34954.html?spm=a2c4g.11186623.6.542.Q67348
 * 队列操作具体接口参考：https://help.aliyun.com/document_detail/27430.html?spm=a2c4g.11186623.6.585.myP2CR
 *
 * @Date: Create in 16:40 2018/7/16
 * @Modificd By:
 */
@ConditionalOnBean({MnsHandler.class})
@Component
public class MnsQueueHandler {

  private final Logger logger = LoggerFactory.getLogger(MnsQueueHandler.class);

  @Autowired
  private MNSClient client;

  /**
   * @Author: zhaomeinan
   * @Description: 创建队列
   *
   * 生成本地QueueMeta属性，有关队列属性详细介绍见https://help.aliyun.com/document_detail/27476.html
   * 延时队列，设置meta的delaySeconds    meta.setDelaySeconds()
   *
   * @Date: 17:03 2018/7/16
   * @Modificd By:
   * @Param: [meta]
   * @return: com.aliyun.mns.client.CloudQueue
   * @throw: 请描述异常信息
   */
  public CloudQueue createQueue(QueueMeta meta) {
    if (meta == null || StringUtils.isEmpty(meta.getQueueName())) {
      return null;
    }

    CloudQueue queue = null;

    try {
      queue = client.createQueue(meta);
      logger.info("MNS - 创建队列成功：队列名称: {}", meta.getQueueName());
    } catch (ClientException ce) {
      logger.error(
          "MNS - 创建队列失败：队列名称: {}；异常信息：Something wrong with the network connection between client and MNS service.Please check your network and DNS availablity.Exception class:ClientException",
          meta.getQueueName());
    } catch (ServiceException se) {
      logger.error(
          "MNS - 创建队列失败：队列名称: {}；异常信息：MNS exception requestId:{},errorCode:{},Exception class:ServiceException",
          meta.getQueueName(), se.getRequestId(), se.getErrorCode());
    } catch (Exception e) {
      logger.error(
          "MNS - 创建队列失败：队列名称: {}；异常信息：Unknown exception happened! Exception class:{};Exception message:{}",
          meta.getQueueName(), e.getClass(), e.getMessage());
    }

    return queue;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 删除队列
   * @Date: 17:36 2018/7/16
   * @Modificd By:
   * @Param: [queueName]
   * @return: void
   * @throw: 请描述异常信息
   */
  public void deleteQueue(String queueName) {
    if (StringUtils.isEmpty(queueName)) {
      return;
    }

    try {
      CloudQueue queue = client.getQueueRef(queueName);
      queue.delete();
      logger.info("MNS - 删除队列成功：队列名称: {}", queueName);
    } catch (ClientException ce) {
      logger.error(
          "MNS - 删除队列失败：队列名称: {}；异常信息：Something wrong with the network connection between client and MNS service.Please check your network and DNS availablity.Exception class:ClientException",
          queueName);
    } catch (ServiceException se) {
      logger.error(
          "MNS - 删除队列失败：队列名称: {}；异常信息：MNS exception requestId:{},errorCode:{},Exception class:ServiceException",
          queueName, se.getRequestId(), se.getErrorCode());
    } catch (Exception e) {
      logger.error(
          "MNS - 删除队列失败：队列名称: {}；异常信息：Unknown exception happened! Exception class:{};Exception message:{}",
          queueName, e.getClass(), e.getMessage());
    }
  }

  /**
   * @Author: zhaomeinan
   * @Description: 发送一条消息
   * @Date: 17:53 2018/7/16
   * @Modificd By:
   * @Param: [queueName, message]
   * @return: com.aliyun.mns.model.Message
   * @throw: 请描述异常信息
   */
  public Message producerOneMessage(String queueName, Message message) {
    if (StringUtils.isEmpty(queueName) || message == null || StringUtils
        .isEmpty(message.getMessageBody())) {
      return null;
    }

    Message putMsg = null;
    try {
      CloudQueue queue = client.getQueueRef(queueName);
      putMsg = queue.putMessage(message);
      logger.info("MNS - 成功发送消息到队列；队列名称: {}；messageId: {}", queueName, putMsg.getMessageId());
    } catch (ClientException ce) {
      logger.error(
          "MNS - 发送消息到队列失败；队列名称: {}；异常信息：Something wrong with the network connection between client and MNS service.Please check your network and DNS availablity.Exception class:ClientException",
          queueName);
    } catch (ServiceException se) {
      logger.error(
          "MNS - 发送消息到队列失败；队列名称: {}；异常信息：MNS exception requestId:{},errorCode:{},Exception class:ServiceException",
          queueName, se.getRequestId(), se.getErrorCode());
    } catch (Exception e) {
      logger.error(
          "MNS - 发送消息到队列失败；队列名称: {}；异常信息：Unknown exception happened! Exception class:{};Exception message:{}",
          queueName, e.getClass(), e.getMessage());
    }
    return putMsg;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 批量发送消息
   * @Date: 16:19 2018/7/17
   * @Modificd By:
   * @Param: [queueName, messages]
   * @return: java.util.List<com.aliyun.mns.model.Message>
   * @throw: 请描述异常信息
   */
  public List<Message> producerMessages(String queueName, List<Message> messages) {
    if (StringUtils.isEmpty(queueName) || messages == null || messages.isEmpty()) {
      return null;
    }

    List<Message> putMsg = null;
    try {
      CloudQueue queue = client.getQueueRef(queueName);
      putMsg = queue.batchPutMessage(messages);
      logger.info("MNS - 成功批量发送消息到队列；队列名称: {}", queueName);
    } catch (ClientException ce) {
      logger.error(
          "MNS - 批量发送消息到队列失败；队列名称: {}；异常信息：Something wrong with the network connection between client and MNS service.Please check your network and DNS availablity.Exception class:ClientException",
          queueName);
    } catch (ServiceException se) {
      logger.error(
          "MNS - 批量发送消息到队列失败；队列名称: {}；异常信息：MNS exception requestId:{},errorCode:{},Exception class:ServiceException",
          queueName, se.getRequestId(), se.getErrorCode());
    } catch (Exception e) {
      logger.error(
          "MNS - 批量发送消息到队列失败；队列名称: {}；异常信息：Unknown exception happened! Exception class:{};Exception message:{}",
          queueName, e.getClass(), e.getMessage());
    }
    return putMsg;
  }

  /**
   * @Author: zhaomeinan
   * @Description: 接收和删除消息（批量接收和批量删除）
   *
   * 需要在业务代码里定制
   *
   * @Date: 17:56 2018/7/16
   * @Modificd By:
   * @Param: [queueName]
   * @return: void
   * @throw: 请描述异常信息
   */
  public void consumer(String queueName) {
    if (StringUtils.isEmpty(queueName)) {
      return;
    }

    try {
      CloudQueue queue = client.getQueueRef(queueName);
      Message popMsg = queue.popMessage(); //queue.batchPopMessage()
      if (popMsg != null) {
        logger.info("message handle: " + popMsg.getReceiptHandle());
        logger.info("message body: " + popMsg.getMessageBodyAsString());
        logger.info("message id: " + popMsg.getMessageId());
        logger.info("message dequeue count:" + popMsg.getDequeueCount());

        //删除已经取出消费的消息
        queue.deleteMessage(popMsg.getReceiptHandle());
        logger.info("delete message successfully.\n");
      } else {
        logger.info("message not exist in TestQueue.\n");
      }
    } catch (ClientException ce) {
      logger.error(
          "Something wrong with the network connection between client and MNS service.Please check your network and DNS availablity.Exception class:ClientException");
    } catch (ServiceException se) {
      logger.error("MNS exception requestId:{},errorCode:{},Exception class:ServiceException",
          se.getRequestId(), se.getErrorCode());
    } catch (Exception e) {
      logger.error("Unknown exception happened! Exception class:{};Exception message:{}",
          e.getClass(), e.getMessage());
    }
  }
}
