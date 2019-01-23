package org.ramer.admin.util;

import javax.servlet.http.*;

import org.springframework.util.*;

import com.alibaba.fastjson.*;

import lombok.extern.slf4j.*;
import okhttp3.*;

/** @author ramer */
@Slf4j
public class IpUtils {
  public static String getRealIP(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    if (ip.contains(",")) {
      ip = ip.split(",")[0].trim();
    }
    return ip;
  }

  /**
   * 通过IP获取对应的位置.
   *
   * @param realIP 客户端真实IP
   * @return
   */
  public static String getLocationByIP(String realIP, OkHttpClient okHttpClient) {
    String location;
    try {
      Request request =
          new Request.Builder()
              .url(String.join("=", "http://ip.taobao.com/service/getIpInfo2.php?ip", realIP))
              .build();
      Response response = okHttpClient.newCall(request).execute();
      JSONObject ipInfo = JSON.parseObject(response.body().string());
      log.debug("  IP信息: [{}]", ipInfo);
      if (!ipInfo.getInteger("code").equals(0)) {
        location = "未知登陆地";
      } else {
        JSONObject data = ipInfo.getJSONObject("data");
        String country = data.getString("country");
        String area = data.getString("area");
        String region = data.getString("region");
        String city = data.getString("city");
        StringBuilder locationBuilder = new StringBuilder();
        locationBuilder.append(country);
        if (!StringUtils.isEmpty(area)) {
          locationBuilder.append(",").append(area);
        }
        if (!StringUtils.isEmpty(region)) {
          locationBuilder.append(",").append(region);
        }
        if (!StringUtils.isEmpty(city)) {
          locationBuilder.append(",").append(city);
        }
        location = locationBuilder.toString();
      }
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      location = "未知登陆地";
    }
    return location;
  }
}
