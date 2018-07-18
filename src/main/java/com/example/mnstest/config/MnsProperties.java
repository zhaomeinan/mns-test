package com.example.mnstest.config;

/**
 * @Author: zhaomeinan
 * @Description: 阿里mns消息服务相关配置参数
 * @Date: Create in 16:13 2018/7/16
 * @Modificd By:
 */
public class MnsProperties {

  /**
   * 是否开启功能，默认不开启
   */
  private boolean enable = false;

  /**
   * 消息服务地址
   */
  private String accountEndpoint = "";

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getAccountEndpoint() {
    return accountEndpoint;
  }

  public void setAccountEndpoint(String accountEndpoint) {
    this.accountEndpoint = accountEndpoint;
  }
}
