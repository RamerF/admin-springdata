package org.ramer.admin.controller.manage;

import io.swagger.annotations.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.domain.manage.Roles;
import org.ramer.admin.entity.pojo.manage.RolesPoJo;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.*;
import org.ramer.admin.util.TextUtil;
import org.ramer.admin.validator.RolesValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('global:read','roles:read')")
@RequestMapping("/manage/roles")
@Api(description = "管理端系统角色接口")
@SuppressWarnings("UnusedDeclaration")
public class RolesController {
  @Resource private RolesService service;
  @Resource private ConfigService configService;
  @Resource private MenuService menuService;
  @Resource private PrivilegeService privilegeService;
  @Resource private CommonService commonService;
  @Resource private RolesValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("系统角色管理页面")
  public String index(Map<String, Object> map) {
    return "manage/roles/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取系统角色列表")
  ResponseEntity list(
      @RequestParam("page") String pageStr,
      @RequestParam("size") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return CommonResponse.ok(service.page(criteria, pageAndSize[0], pageAndSize[1]));
  }

  @GetMapping
  @ApiOperation("添加系统角色页面")
  String create(Map<String, Object> map) {
    map.put("menus", menuService.list(null));
    map.put("privileges", privilegeService.list(null));
    return "manage/roles/create";
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('global:create','roles:create')")
  @ResponseBody
  @ApiOperation("添加系统角色")
  ResponseEntity create(
      @RequestParam("menuIds[]") String[] menuIdsStr,
      @RequestParam("privilegeIds[]") String[] privilegeIdsStr,
      @Valid Roles roles,
      BindingResult bindingResult)
      throws Exception {
    log.info(" RolesController.create : [{}]", roles);
    if (bindingResult.hasErrors()) {
      StringBuilder errorMsg = new StringBuilder("提交信息有误: ");
      bindingResult
          .getAllErrors()
          .forEach(error -> errorMsg.append("\n").append(error.getDefaultMessage()));
      return CommonResponse.fail(errorMsg.toString());
    }
    List<Long> menuIds = TextUtil.validLongs(menuIdsStr);
    if (menuIds.size() == 0) {
      return CommonResponse.fail("菜单参数有误");
    }
    List<Long> privilegeIds = TextUtil.validLongs(privilegeIdsStr);
    if (privilegeIds.size() == 0) {
      return CommonResponse.fail("权限参数有误");
    }
    roles = service.create(roles, menuIds, privilegeIds);
    return roles != null && roles.getId() > 0
        ? CommonResponse.ok(null, "保存成功")
        : CommonResponse.fail("保存失败,请稍后再试");
  }

  @GetMapping("/{id}")
  @ApiOperation("更新系统角色页面")
  String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) {
      throw new NumberFormatException("id 格式不正确");
    }
    final Roles roles = service.getById(id);
    map.put(
        "selectedMenuIds",
        roles.getMenus().stream().map(AbstractEntity::getId).collect(Collectors.toList()));
    map.put(
        "selectedPrivilegeIds",
        roles.getPrivileges().stream().map(AbstractEntity::getId).collect(Collectors.toList()));
    map.put("menus", menuService.list(null));
    map.put("privileges", privilegeService.list(null));
    return commonService.update(
        service, RolesPoJo.class, idStr, "manage/roles/update", map, "roles");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','roles:write')")
  @ApiOperation("更新系统角色")
  ResponseEntity update(
      @PathVariable("id") String idStr,
      @RequestParam(value = "menuIds[]", required = false) String[] menuIdsStr,
      @RequestParam(value = "privilegeIds[]", required = false) String[] privilegeIdsStr,
      @Valid Roles roles,
      BindingResult bindingResult) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    if (menuIdsStr == null) {
      return CommonResponse.canNotBlank("菜单");
    }
    if (privilegeIdsStr == null) {
      return CommonResponse.canNotBlank("权限");
    }
    if (bindingResult.hasErrors()) {
      StringBuilder errorMsg = new StringBuilder("提交信息有误: ");
      bindingResult
          .getAllErrors()
          .forEach(error -> errorMsg.append("\n").append(error.getDefaultMessage()));
      return CommonResponse.fail(errorMsg.toString());
    }
    roles.setId(id);
    List<Long> menuIds = TextUtil.validLongs(menuIdsStr);
    if (menuIds.size() == 0) {
      return CommonResponse.fail("菜单参数有误");
    }
    List<Long> privilegeIds = TextUtil.validLongs(privilegeIdsStr);
    if (privilegeIds.size() == 0) {
      return CommonResponse.fail("权限参数有误");
    }
    try {
      roles = service.update(roles, menuIds, privilegeIds);
      return roles == null
          ? CommonResponse.fail("记录不存在")
          : roles.getId() > 0 ? CommonResponse.ok(null, "更新成功") : CommonResponse.fail("更新失败");
    } catch (Exception e) {
      log.warn(" RolesController.update : [{}]", e.getMessage());
      return CommonResponse.fail("更新失败,数据格式异常");
    }
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','roles:delete')")
  @ApiOperation("删除系统角色")
  ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    return commonService.delete(service, idStr);
  }
}
