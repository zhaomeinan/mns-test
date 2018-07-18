package com.example.mnstest.handler.mns;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.*;

/**
 * @Author: zhaomeinan
 * @Description: 推送类型为http的MNS topic消息，消费消息前预处理(校验请求是否来自mns、解析消息内容)
 * @Date: Create in 14:27 2018/7/17
 * @Modificd By:
 */
@Component
public class HttpEndpointHandler {

  /**
   * @Author: zhaomeinan
   * @Description: 校验请求是否来自mns
   * @Date: 14:37 2018/7/16
   * @Modificd By:
   * @Param: [request]
   * @return: java.lang.Boolean
   * @throw: 请描述异常信息
   */
  public Boolean isFromMns(HttpServletRequest request) {
    String method = request.getMethod();

    if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
      return false;
    }

    //header信息
    Enumeration<String> headerNamess = request.getHeaderNames();
    Map<String, String> hm = new HashMap<String, String>();
    for (Enumeration e = headerNamess; e.hasMoreElements(); ) {
      String thisName = e.nextElement().toString();
      String thisValue = request.getHeader(thisName);
      hm.put(thisName, thisValue);
    }

    //uri
    String target = request.getRequestURI();

    //verify request
    String cert = request.getHeader("x-mns-signing-cert-url");
    if (StringUtils.isEmpty(cert)) {
      return false;
    }
    cert = new String(Base64.decodeBase64(cert));

    return authenticate(method, target, hm, cert);
  }

  /**
   * check if this request comes from MNS Server
   *
   * @param method, http method
   * @param uri, http uri
   * @param headers, http headers
   * @param cert, cert url
   * @return true if verify pass
   */
  private Boolean authenticate(String method, String uri, Map<String, String> headers,
      String cert) {
    String str2sign = getSignStr(method, uri, headers);
    //System.out.println(str2sign);
    String signature = headers.get("authorization");
    if (StringUtils.isEmpty(signature)) {
      return false;
    }
    byte[] decodedSign = Base64.decodeBase64(signature);
    //get cert, and verify this request with this cert
    try {
      //String cert = "http://mnstest.oss-cn-hangzhou.aliyuncs.com/x509_public_certificate.pem";
      URL url = new URL(cert);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      DataInputStream in = new DataInputStream(conn.getInputStream());
      CertificateFactory cf = CertificateFactory.getInstance("X.509");

      Certificate c = cf.generateCertificate(in);
      PublicKey pk = c.getPublicKey();

      java.security.Signature signetcheck = java.security.Signature.getInstance("SHA1withRSA");
      signetcheck.initVerify(pk);
      signetcheck.update(str2sign.getBytes());
      Boolean res = signetcheck.verify(decodedSign);
      return res;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * build string for sign
   *
   * @param method, http method
   * @param uri, http uri
   * @param headers, http headers
   * @return String fro sign
   */
  private String getSignStr(String method, String uri, Map<String, String> headers) {
    StringBuilder sb = new StringBuilder();
    sb.append(method);
    sb.append("\n");
    sb.append(safeGetHeader(headers, "content-md5"));
    sb.append("\n");
    sb.append(safeGetHeader(headers, "content-type"));
    sb.append("\n");
    sb.append(safeGetHeader(headers, "date"));
    sb.append("\n");

    List<String> tmp = new ArrayList<String>();
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      if (entry.getKey().startsWith("x-mns-")) {
        tmp.add(entry.getKey() + ":" + entry.getValue());
      }
    }
    Collections.sort(tmp);

    for (String kv : tmp) {
      sb.append(kv);
      sb.append("\n");
    }

    sb.append(uri);
    return sb.toString();
  }

  private String safeGetHeader(Map<String, String> headers, String name) {
    if (headers.containsKey(name)) {
      return headers.get(name);
    } else {
      return "";
    }
  }

  /**
   * @Author: zhaomeinan
   * @Description: 获取消息信息
   * @Date: 14:38 2018/7/16
   * @Modificd By:
   * @Param: [request]
   * @return: com.example.mnstest.handler.MnsTopicNotification
   * @throw: 请描述异常信息
   */
  public MnsTopicNotification getMessage(HttpServletRequest request) throws IOException {
    ServletInputStream is = request.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuffer buffer = new StringBuffer();
    String line = "";
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }

    is.close();
    reader.close();

    ObjectMapper mapper = new ObjectMapper();
    MnsTopicNotification mnsTopicNotification = mapper
        .readValue(buffer.toString(), MnsTopicNotification.class);

    return mnsTopicNotification;
  }
}
