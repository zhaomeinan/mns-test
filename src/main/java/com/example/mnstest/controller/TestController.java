package com.example.mnstest.controller;

import com.example.mnstest.config.AppProperties;
import com.example.mnstest.handler.mns.HttpEndpointHandler;
import com.example.mnstest.handler.mns.MnsTopicNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: zhaomeinan
 * @Description: 接收mns的消息(topic模型)
 * @Date: 18:10 2018/7/17
 * @Modificd By:
 * @Param:
 * @return:
 * @throw: 请描述异常信息
 */
@RestController
public class TestController {

  @Autowired
  private HttpEndpointHandler httpEndpointHandler;

  @Autowired
  private AppProperties appProperties;

  @GetMapping("not-mns-called")
  public String testGet(HttpServletRequest request) {
    System.out.println("接收了一条消息");
    Boolean b = httpEndpointHandler.isFromMns(request);
    System.out.println(b);
    return "你好，世界";
  }

  /**
   * @Author: zhaomeinan
   * @Description: 测试来自mns的请求
   * @Date: 15:10 2018/7/18
   * @Modificd By:
   * @Param: [request]
   * @return: java.lang.String
   * @throw: 请描述异常信息
   */
  @PostMapping("mns-called")
  public String testPost(HttpServletRequest request) throws IOException {
    System.out.println("接收了一条消息 in post");

    //校验请求是否来自mns
    Boolean b = httpEndpointHandler.isFromMns(request);

    System.out.println(b);

    String retMsg = "";
    if (b) {
      //解析传入参数
      MnsTopicNotification message = httpEndpointHandler.getMessage(request);

      ObjectMapper mapper = new ObjectMapper();

      retMsg = mapper.writeValueAsString(message);

      System.out.println("接收到的消息：" + retMsg);

      System.out.println("接收到的消息内容为：" + message.getMessage());
    }

    return retMsg;
  }
}
