/*
 *
 */
package org.ramer.admin.util;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 加密工具类.
 *
 * @author ramer
 */
@Slf4j
@Component
public class EncryptUtil {
  private static PasswordEncoder passwordEncoder;

  @Resource
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    EncryptUtil.passwordEncoder = passwordEncoder;
  }

  //    public void initBCrymptPasswordEncoder() {
  //        encoder = new BCryptPasswordEncoder(8, new SecureRandom("ramer".getBytes()));
  //    }

  public static String execEncrypt(Object string) {
    String encode = passwordEncoder.encode(string.toString());
    log.info("  encrypt: [{}],[{}]", string, encode);
    return encode;
  }

  public static boolean matches(String plainPassword, String encodedPassword) {
    boolean matches = passwordEncoder.matches(plainPassword, encodedPassword);
    log.info("  matches password: [{}]", matches);
    return matches;
  }
}
