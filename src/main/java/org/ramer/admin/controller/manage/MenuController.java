package org.ramer.admin.controller.manage;

import com.alibaba.fastjson.JSONObject;
import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.MenuService;
import org.ramer.admin.util.TextUtil;
import org.ramer.admin.validator.MenuValidator;
import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/manage/menu")
@PreAuthorize("hasAnyAuthority('global:read','menu:read')")
@Api(description = "管理端系统菜单接口")
@SuppressWarnings("UnusedDeclaration")
public class MenuController {
  @Resource private MenuService service;
  @Resource private CommonService commonService;
  @Resource private MenuValidator validator;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("系统菜单页面")
  String index() {
    return "manage/menu/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取系统菜单列表")
  ResponseEntity list(
      @RequestParam("page") String pageStr,
      @RequestParam("size") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return CommonResponse.ok(service.page(criteria, pageAndSize[0], pageAndSize[1]));
  }

  @GetMapping
  @ApiOperation("添加系统菜单页面")
  String create(Map<String, Object> map) {
    map.put("menus", service.list(null));
    return "manage/menu/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','menu:create')")
  @ApiOperation("添加系统菜单")
  ResponseEntity create(
      @ApiParam("父级菜单Id") @RequestParam("parentId") String parentIdStr,
      @Valid Menu menu,
      BindingResult bindingResult)
      throws Exception {
    log.info(" MenuController.create : [{},{}]", menu, parentIdStr);
    final long parentId = TextUtil.validLong(parentIdStr, 0);
    if (StringUtils.isEmpty(menu.getAlia())) {
      JSONObject jsonObject = new JSONObject();
      return CommonResponse.fail("提交信息有误: \n权限别名 不能为空且小于25个字符");
    }
    if (parentId > 0) {
      menu.setParent(Menu.of(parentId));
    }
    return commonService.create(service, menu, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新系统菜单页面")
  String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    map.put("menus", service.list(null));
    return commonService.update(service, idStr, "manage/menu/update", map, "menu");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','menu:write')")
  @ApiOperation("更新系统菜单")
  ResponseEntity update(
      @PathVariable("id") String idStr,
      @RequestParam("parentId") String parentIdStr,
      @Valid Menu menu,
      BindingResult bindingResult)
      throws Exception {
    log.info(" MenuController.update : [{},{}]", menu, parentIdStr);
    final long parentId = TextUtil.validLong(parentIdStr, 0);
    if (parentId < 0) return CommonResponse.wrongFormat("parentId");
    if (parentId > 0) {
      menu.setParent(Menu.of(parentId));
    }
    return commonService.update(service, menu, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','menu:delete')")
  @ApiOperation("删除系统菜单")
  ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" MenuController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
