package com.example.mnstest.handler.mns;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @Author: zhaomeinan
 * @Description: mns topic  消息实体信息
 * @Date: Create in 13:29 2018/7/16
 * @Modificd By:
 */
public class MnsTopicNotification implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 消息内容(解码后的实际内容)
   */
  @JsonProperty("Message")
  private String message;

  /**
   * 消息ID
   */
  @JsonProperty("MessageId")
  private String messageId;

  /**
   * 消息体的MD5
   */
  @JsonProperty("MessageMD5")
  private String messageMD5;

  /**
   * 消息发布时间
   */
  @JsonProperty("PublishTime")
  private Long publishTime;

  /**
   * 订阅者所属账号(eg：1783322637466422)
   */
  @JsonProperty("Subscriber")
  private String subscriber;

  /**
   * 订阅者名称
   */
  @JsonProperty("SubscriptionName")
  private String subscriptionName;

  /**
   * 主题名称
   */
  @JsonProperty("TopicName")
  private String topicName;

  /**
   * 主题所属账号(eg：1783322637466422)
   */
  @JsonProperty("TopicOwner")
  private String topicOwner;

  @JsonProperty("SigningCertURL")
  private String signingCertURL;

  /**
   * 发布时间，字符串格式
   */
  private String publishTimeStr;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = new String(Base64.decodeBase64(message));
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getMessageMD5() {
    return messageMD5;
  }

  public void setMessageMD5(String messageMD5) {
    this.messageMD5 = messageMD5;
  }

  public Long getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Long publishTime) {
    this.publishTime = publishTime;
  }

  public String getSubscriber() {
    return subscriber;
  }

  public void setSubscriber(String subscriber) {
    this.subscriber = subscriber;
  }

  public String getSubscriptionName() {
    return subscriptionName;
  }

  public void setSubscriptionName(String subscriptionName) {
    this.subscriptionName = subscriptionName;
  }

  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public String getTopicOwner() {
    return topicOwner;
  }

  public void setTopicOwner(String topicOwner) {
    this.topicOwner = topicOwner;
  }

  public String getSigningCertURL() {
    return signingCertURL;
  }

  public void setSigningCertURL(String signingCertURL) {
    this.signingCertURL = signingCertURL;
  }

  public String getPublishTimeStr() {
    return publishTimeStr;
  }

  public void setPublishTimeStr(String publishTimeStr) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    this.publishTimeStr = sdf.format(publishTime);
  }
}
