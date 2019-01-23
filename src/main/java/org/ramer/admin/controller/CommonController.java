package org.ramer.admin.controller;

import org.ramer.admin.entity.domain.manage.ManageLog;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.service.manage.system.ManageLogService;
import org.ramer.admin.service.manage.system.ManagerService;
import org.ramer.admin.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/** @author ramer */
@Slf4j
@Controller("common")
@Api(description = "全局通用接口")
public class CommonController {
  @Resource private ManagerService managerService;
  @Resource private ManageLogService manageLogService;

  @GetMapping("/400")
  @ApiOperation("400错误")
  public String error400() {
    return "400";
  }

  @GetMapping("/401")
  @ApiOperation("401错误")
  public String error401() {
    return "401";
  }

  @GetMapping("/404")
  @ApiOperation("404错误")
  public String error404() {
    return "404";
  }

  @GetMapping("/templateEngineException")
  @ApiOperation("模板引擎异常")
  public String templateEngineException(HttpServletRequest request, Map<String, Object> map) {
    map.put("error", "Internal Server Error");
    log.info(" CommonController.templateEngineException : [{}]", request.getRequestURL());
    return "500";
  }

  @RequestMapping("/forbidden")
  @ResponseBody
  @ApiOperation("拒绝访问")
  public ResponseEntity forbidden(HttpServletRequest request, Authentication authentication) {
    ManageLog manageLogs = new ManageLog();
    manageLogs.setIp(IpUtils.getRealIP(request));
    if (authentication != null && authentication.getName() != null) {
      manageLogs.setManager(managerService.getByEmpNo(authentication.getName()));
    }
    manageLogs.setUrl(request.getRequestURL().toString());
    manageLogs.setResult(new AccessDeniedException("拒绝访问").toString());
    log.warn(
        " CommonController.forbidden : [{},{}]",
        request.getRequestURL(),
        authentication == null ? authentication : authentication.getName());
    try {
      manageLogService.create(manageLogs);
    } catch (Exception e1) {
      log.warn("记录操作日志失败");
    }
    return new ResponseEntity<>(CommonResponse.fail("拒绝访问"), HttpStatus.FORBIDDEN);
  }
}
