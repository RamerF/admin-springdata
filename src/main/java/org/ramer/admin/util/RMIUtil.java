package org.ramer.admin.util;

import java.nio.charset.Charset;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@Slf4j
public class RMIUtil {
  /**
   * 调用内部远程接口.
   *
   * @param code
   * @param data Base64 加密
   * @param signed secret 加上 base64 data一起MD5加密
   */
  public <T> T invoke(
      String url,
      String code,
      String data,
      String signed,
      RequestMethod requestMethod,
      Class<T> clazz) {
    log.debug(" RemoteInvoke.invoke : [{},{},{},{},{}]", url, code, data, signed, requestMethod);
    HashMap<String, String> params = null;
    if (data != null) {
      params = new HashMap<>();
      params.put("code", code);
      params.put("signed", signed);
      params.put("data", data);
    }
    return HttpUtils.request(url, params, requestMethod, clazz);
  }

  public static String base64Encode(String string) {
    return Base64Utils.encodeToString(string.getBytes(Charset.forName("UTF-8")));
  }

  public static String base64Decode(String string) {
    return new String(
        Base64Utils.decode(string.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8"));
  }

  public static String md5Encode(String string) {
    return DigestUtils.md5DigestAsHex(string.getBytes());
  }
}
