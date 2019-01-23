package org.ramer.admin.controller.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.*;
import org.ramer.admin.util.*;
import org.ramer.admin.util.PathUtil.SavingFolder;
import org.ramer.admin.validator.ManagerValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/manage/manager")
@PreAuthorize("hasAnyAuthority('global:read','manager:read')")
@Api(description = "管理端系统操作员接口")
@SuppressWarnings("UnusedDeclaration")
public class ManagerController {
  @Resource private ManagerService service;
  @Resource private MenuService menuService;
  @Resource private RolesService rolesService;
  @Resource private CommonService commonService;
  @Resource private ManagerValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("管理员管理页面")
  String index(Map<String, Object> map) {
    return "manage/manager/index";
  }

  @GetMapping("/list")
  @ResponseBody
  ResponseEntity list(
      @RequestParam("page") String pageStr,
      @RequestParam("size") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return CommonResponse.ok(service.page(criteria, pageAndSize[0], pageAndSize[1]));
  }

  @GetMapping
  @ApiOperation("添加管理员管理页面")
  String create(Map<String, Object> map) {
    map.put("roleses", rolesService.list(null));
    return "manage/manager/create";
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('global:create','manager:create')")
  @ResponseBody
  @ApiOperation("添加管理员")
  ResponseEntity create(
      @RequestParam(value = "validDate", required = false) String validDateStr,
      @RequestParam(value = "roleIds[]", required = false) String[] roleIdsStr,
      @Valid Manager manager,
      BindingResult bindingResult)
      throws Exception {
    log.info(
        " ManagerController.create : [{},{},{}]",
        JSON.toJSONString(manager),
        validDateStr,
        roleIdsStr);
    if (StringUtils.isEmpty(manager.getPassword())
        || !manager.getPassword().matches("^[a-zA-Z]\\w{5,17}$")) {
      return CommonResponse.fail("密码 必须以字母开头,长度在6~18之间,只能包含字符,数字和下划线");
    }
    Date validDate = TextUtil.validDate(validDateStr, "yyyy-MM-dd HH:mm:ss", null);
    manager.setValidDate(validDate);
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(commonService.collectBindingResult(bindingResult));
    }
    try {
      manager =
          service.save(
              manager,
              Stream.of(Optional.ofNullable(roleIdsStr).orElseGet(() -> new String[] {}))
                  .map((validLong) -> TextUtil.validLong(validLong, 0))
                  .filter(roleId -> roleId != 0)
                  .collect(Collectors.toList()));
      return manager == null
          ? CommonResponse.fail("记录已存在")
          : manager.getId() > 0 ? CommonResponse.ok(null) : CommonResponse.fail("添加失败");
    } catch (Exception e) {
      log.warn(" ManagerController.create : [{}]", e.getMessage());
      return CommonResponse.fail("添加失败,数据格式异常");
    }
  }

  @GetMapping("/{id}")
  @ApiOperation("更新管理员页面")
  String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) {
      throw new CommonException("id 格式不正确");
    }
    map.put("roleses", rolesService.list(null));
    map.put(
        "selectedRolesIds",
        Optional.ofNullable(rolesService.listByManager(id)).orElse(new ArrayList<>()).stream()
            .map(AbstractEntity::getId)
            .collect(Collectors.toList()));
    return commonService.update(service, idStr, "manage/manager/update", map, "manager");
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('global:write','manager:write')")
  @ResponseBody
  @ApiOperation("更新管理员")
  ResponseEntity update(
      @PathVariable("id") String idStr,
      @RequestParam(value = "validDate", required = false) String validDateStr,
      @RequestParam(value = "roleIds[]", required = false) String[] roleIdsStr,
      @Valid Manager manager,
      BindingResult bindingResult)
      throws Exception {
    log.info(" ManagerController.update : [{},{}]", JSON.toJSONString(manager), validDateStr);
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    Date validDate = TextUtil.validDate(validDateStr, "yyyy-MM-dd HH:mm:ss", null);
    manager.setValidDate(validDate);
    manager.setId(id);
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(commonService.collectBindingResult(bindingResult));
    }
    if (!StringUtils.isEmpty(manager.getPassword())) {
      manager.setPassword(EncryptUtil.execEncrypt(manager.getPassword()));
    }
    try {
      manager =
          service.update(
              manager,
              Stream.of(Optional.ofNullable(roleIdsStr).orElseGet(() -> new String[] {}))
                  .map((validLong) -> TextUtil.validLong(validLong, 0))
                  .filter(roleId -> roleId != 0)
                  .collect(Collectors.toList()));
      return manager == null
          ? CommonResponse.fail("记录不存在")
          : manager.getId() > 0 ? CommonResponse.ok(null, "更新成功") : CommonResponse.fail("更新失败");
    } catch (Exception e) {
      log.warn(" ManagerController.update : [{}]", e.getMessage());
      return CommonResponse.fail("更新失败,数据格式异常");
    }
  }

  @PutMapping("/roles/{id}")
  @PreAuthorize("hasAnyAuthority('global:write','manager:write')")
  @ResponseBody
  @ApiOperation("更新管理员角色")
  ResponseEntity create(
      @PathVariable("id") String idStr, @RequestParam("roleIds[]") String[] roleIdsStr) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    Manager manager = service.getById(id);
    manager =
        service.update(
            manager,
            Stream.of(roleIdsStr)
                .map((validLong) -> TextUtil.validLong(validLong, 0))
                .filter(roleId -> roleId != 0)
                .collect(Collectors.toList()));
    if (manager != null && manager.getId() > 0) {
      return CommonResponse.ok(null);
    }
    return CommonResponse.fail("更新角色失败,请稍后再试");
  }

  @RequestMapping("/setImg/{id}")
  @ResponseBody
  ResponseEntity setImg(@PathVariable("id") String idStr, @RequestParam("file") MultipartFile file)
      throws Exception {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    Manager manager = service.getById(id);
    // 获取shop图片目录的相对值路径
    String imageUrl;
    try {
      imageUrl =
          ImageUtil.generateThumbnail(
              file.getInputStream(),
              file.getOriginalFilename(),
              PathUtil.getImagePath(SavingFolder.MANAGER));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    manager.setAvatar(imageUrl);
    final Manager update = service.update(manager);
    return update != null && update.getId() > 0
        ? CommonResponse.ok(null)
        : CommonResponse.fail("更新失败");
  }

  //    @RequestMapping("/getImg")
  //    @ResponseBody
  //    ResponseEntity setImg(@RequestParam("id") Long id, ResourceHandlerRegistry registry) {
  //        Manager person = service.getById(id);
  //        // 获取shop图片目录的相对值路径
  //        String imageUrl = person.getAvatar();
  //        File file = new File(PathUtil.getResourceBasePath() + imageUrl, "userImage");
  //
  // registry.addResourceHandler("/image/**").addResourceLocations(PathUtil.getResourceBasePath() +
  // imageUrl);
  //        return null;
  //    }

  @GetMapping("/getRolesAndMenus")
  @ResponseBody
  ResponseEntity initUserInfoById(HttpSession session) {
    final Manager person = (Manager) session.getAttribute("manager");
    return Optional.ofNullable(service.getById(person.getId()))
        .map(
            manager -> {
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("userName", manager.getName());
              jsonObject.put("validate", manager.getValidDate().getTime());
              jsonObject.put("phone", manager.getPhone());
              jsonObject.put("gender", manager.getGender());
              jsonObject.put("roles", rolesService.listNameByManager(manager.getId()));
              jsonObject.put("menus", menuService.listNameByManager(manager.getId()));
              return CommonResponse.ok(jsonObject);
            })
        .orElse(CommonResponse.ok(new JSONObject()));
  }

  @PutMapping("/password/{id}")
  @PreAuthorize(
      "isAuthenticated() and #session.getAttribute('manager') != null and #session.getAttribute('manager').id.toString() eq #idStr")
  @ResponseBody
  @ApiOperation("更新管理员自己的密码")
  ResponseEntity updatePassword(
      @PathVariable("id") @P("idStr") String idStr,
      @RequestParam(value = "oldPass") String oldPass,
      @RequestParam(value = "newPass") String newPass,
      @P("manager") HttpSession session) {
    Manager manager = (Manager) session.getAttribute("manager");
    int result = service.updatePassword(manager.getId(), oldPass, newPass);
    ResponseEntity commonResponse;
    switch (result) {
      case -2:
        commonResponse = CommonResponse.fail("原密码输入不正确");
        break;
      case -1:
        commonResponse = CommonResponse.fail("原密码输入不正确");
        break;
      case 0:
        commonResponse = CommonResponse.fail("原密码输入不正确");
        break;
      default:
        commonResponse = CommonResponse.ok(null, "修改密码成功");
    }
    if (result > 0) {
      session.invalidate();
    }
    return commonResponse;
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('global:delete','manager:delete')")
  @ResponseBody
  ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    return commonService.delete(service, idStr);
  }
}
