package org.ramer.admin.controller.manage;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant.AccessPath;
import org.ramer.admin.entity.domain.manage.Config;
import org.ramer.admin.entity.pojo.manage.ConfigPoJo;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.ConfigService;
import org.ramer.admin.util.TextUtil;
import org.ramer.admin.validator.ConfigValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('global:read','config:read')")
@RequestMapping(AccessPath.MANAGE + "/config")
@Api(description = "管理端系统参数接口")
@SuppressWarnings("UnusedDeclaration")
public class ConfigController {
  @Resource private ConfigService service;
  @Resource private CommonService commonService;
  @Resource private ConfigValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("系统参数页面")
  String index() {
    return "manage/config/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取系统参数列表")
  ResponseEntity list(
      @RequestParam("page") String pageStr,
      @RequestParam("size") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return CommonResponse.ok(service.page(criteria, pageAndSize[0], pageAndSize[1]));
  }

  @GetMapping
  @ApiOperation("添加系统参数页面")
  String create() {
    return "manage/config/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','config:create')")
  @ApiOperation("添加系统参数")
  ResponseEntity create(@Valid Config config, BindingResult bindingResult) throws Exception {
    log.info(" ConfigController.create : [{}]", config);
    return commonService.create(service, config, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新系统参数页面")
  String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service, ConfigPoJo.class, idStr, "manage/config/update", map, "config");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','config:write')")
  @ApiOperation("更新系统参数")
  ResponseEntity update(
      @PathVariable("id") String idStr, @Valid Config config, BindingResult bindingResult)
      throws Exception {
    log.info(" ConfigController.update : [{}]", config);
    return commonService.update(service, config, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','config:delete')")
  @ApiOperation("删除系统参数")
  ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" ConfigController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
