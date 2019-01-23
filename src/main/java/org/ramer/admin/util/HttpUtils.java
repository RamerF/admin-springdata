package org.ramer.admin.util;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;

/** @author ramer */
@Component
@Slf4j
public class HttpUtils {
  private static OkHttpClient okHttpClient;

  @Resource
  public void setOkHttpClient(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
  }

  public static <T> T request(
      String url, Map<String, String> data, RequestMethod requestMethod, Class<T> clazz) {
    log.debug(" HttpUtils.request : [{},{},{}]", url, JSON.toJSONString(data), requestMethod);
    FormBody.Builder formBuilder = new FormBody.Builder();
    if (data != null && data.size() > 0) {
      boolean isGetMethod = requestMethod.equals(RequestMethod.GET);
      StringBuilder urlBuilder = new StringBuilder(url);
      String finalUrl = url;
      data.forEach(
          (key, val) -> {
            formBuilder.add(key, val);
            if (isGetMethod) {
              if (urlBuilder.length() == finalUrl.length()) {
                urlBuilder.append("?");
              } else {
                urlBuilder.append("&");
              }
              urlBuilder.append(key).append("=").append(val);
            }
          });
      url = urlBuilder.toString();
    }
    log.info(" HttpUtils.request : [{}]", url);
    FormBody requestBody = formBuilder.build();
    Builder builder = new Request.Builder().url(url);
    switch (requestMethod) {
      case GET:
        builder.get();
        break;
      case POST:
        builder.post(requestBody);
        break;
      case PUT:
        builder.put(requestBody);
        break;
      case DELETE:
        builder.delete(requestBody);
        break;
      default:
        log.error("request method {} is not supported", requestMethod);
    }
    try {
      ResponseBody body = okHttpClient.newCall(builder.build()).execute().body();
      String result = body.string();
      log.debug(" HttpUtils.request : response [{}]", result);
      return JSON.parseObject(result, clazz);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
