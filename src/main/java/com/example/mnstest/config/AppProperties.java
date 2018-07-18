package com.example.mnstest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhaomeinan
 * @Description: 系统自定义参数
 * @Date: Create in 16:10 2018/7/16
 * @Modificd By:
 */
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppProperties {

  /**
   * 阿里云相关功能配置
   */
  private AliyunProperties aliyun;

  public AliyunProperties getAliyun() {
    return aliyun;
  }

  public void setAliyun(AliyunProperties aliyun) {
    this.aliyun = aliyun;
  }
}
