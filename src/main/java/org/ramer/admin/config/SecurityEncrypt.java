package org.ramer.admin.config;

import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.service.manage.system.ManagerService;
import org.ramer.admin.util.EncryptUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/** Created by RAMER on 5/25/2017. */
@Slf4j
@Component
public class SecurityEncrypt implements AuthenticationProvider {
  @Resource private UserDetailsService userService;
  @Resource private ManagerService managerService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String empNo = authentication.getName();
    Object credentials = authentication.getCredentials();
    log.debug(" empNo : {}", empNo);
    log.debug(" password : {}", credentials);
    UserDetails user = userService.loadUserByUsername(empNo);
    if (!EncryptUtil.matches(credentials.toString(), user.getPassword())) {
      managerService.setLoginStatus(empNo);
      throw new BadCredentialsException("bad password");
    }
    final Manager manager = managerService.getByEmpNo(empNo);
    if (manager.getState().equals(Constant.STATE_OFF)
        || manager.getValidDate() == null
        || manager.getValidDate().before(new Date())
        || manager.getActive().equals(Constant.ACTIVE_FALSE)) {
      throw new BadCredentialsException("bad password");
    }
    return new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
