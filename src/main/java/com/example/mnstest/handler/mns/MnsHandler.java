package com.example.mnstest.handler.mns;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import com.example.mnstest.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: zhaomeinan
 * @Description: 注册MNSClient
 * @Date: Create in 16:30 2018/7/16
 * @Modificd By:
 */
@ConditionalOnProperty(name = "app.aliyun.mns.enable", havingValue = "true")
@Component
public class MnsHandler {

  @Autowired
  private AppProperties appProperties;

  @Bean
  public MNSClient getMNSClient() {
    CloudAccount account = new CloudAccount(appProperties.getAliyun().getAccessKeyId(),
        appProperties.getAliyun().getAccessKeySecret(),
        appProperties.getAliyun().getMns().getAccountEndpoint());
    return account.getMNSClient();
  }
}
