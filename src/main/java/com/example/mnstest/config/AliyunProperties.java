package com.example.mnstest.config;

/**
 * @Author: zhaomeinan
 * @Description: 阿里云配置相关参数
 * @Date: Create in 15:34 2018/7/18
 * @Modificd By:
 */
public class AliyunProperties {

  /**
   * 阿里access key id，无需配置
   */
  private String accessKeyId = "";

  /**
   * 阿里access key secret，无需配置
   */
  private String accessKeySecret = "";

  /**
   * MNS消息服务相关
   */
  private MnsProperties mns;

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getAccessKeySecret() {
    return accessKeySecret;
  }

  public void setAccessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
  }

  public MnsProperties getMns() {
    return mns;
  }

  public void setMns(MnsProperties mns) {
    this.mns = mns;
  }
}
