package org.ramer.admin.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.entity.response.manage.MenuResponse;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.service.common.BaseService;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.ConfigService;
import org.ramer.admin.service.manage.system.MenuService;
import org.ramer.admin.util.TextUtil;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/** @author ramer */
@Service("manageCommon")
@Slf4j
public class CommonServiceImpl implements CommonService {
  @Resource private MenuService menuService;
  @Resource private ConfigService configService;

  @Override
  public void writeMenuAndSiteInfo(HttpSession session, Map<String, Object> map) {
    final Manager manager = (Manager) session.getAttribute("manager");
    final Long managerId = manager.getId();
    final List<MenuPoJo> menuPoJos = menuService.listNameByManager(managerId);
    // 所有可用菜单
    final List<MenuResponse> menusAll = new ArrayList<>();
    // 可用菜单的树形结构
    final List<MenuResponse> menus = new ArrayList<>();
    menuPoJos.forEach(
        menuPoJo ->
            menusAll.add(
                new MenuResponse(
                    menuPoJo.getId(),
                    menuPoJo.getName(),
                    menuPoJo.getUrl(),
                    menuPoJo.getLeaf(),
                    menuPoJo.getIcon(),
                    menuPoJo.getPId())));
    menuPoJos
        .stream()
        .filter(menuPoJo -> menuPoJo.getPId() == null)
        .forEach(
            menuPoJo -> {
              log.info(" CommonServiceImpl.writeMenuAndSiteInfo : [{}]", menuPoJo.getId());
              menus.add(
                  new MenuResponse(
                      menuPoJo.getId(),
                      menuPoJo.getName(),
                      menuPoJo.getUrl(),
                      menuPoJo.getLeaf(),
                      menuPoJo.getIcon(),
                      menuPoJo.getPId()));
            });
    menusAll.removeAll(menus);
    Stack<MenuResponse> retain = new Stack<>();
    menus.forEach(retain::push);
    while (retain.size() > 0 && menusAll.size() > 0) {
      MenuResponse menu = retain.pop();
      // 当前节点的子节点
      menusAll
          .stream()
          .filter(menuResponse -> menuResponse.getPId().equals(menu.getId()))
          .filter(menuResponse -> !menuResponse.getLeaf())
          .forEach(retain::push);
      // 子节点具有叶子节点,入栈
      List<MenuResponse> childResponse =
          menusAll
              .stream()
              .filter(menuResponse -> menuResponse.getPId().equals(menu.getId()))
              .collect(Collectors.toList());
      menu.setChildren(childResponse);
      menusAll.removeAll(childResponse);
    }
    map.put("menus", menus);
    JSONObject siteJson = new JSONObject();
    siteJson.put("title", configService.getSiteInfo(Constant.SITE_TITLE));
    siteJson.put("name", configService.getSiteInfo(Constant.SITE_NAME));
    siteJson.put("copyright", configService.getSiteInfo(Constant.SITE_COPYRIGHT));
    map.put("site", siteJson);
  }

  @Override
  public <T extends BaseService<E, U>, E extends AbstractEntity, U> ResponseEntity create(
      T invoke, E entity, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(collectBindingResult(bindingResult));
    }
    try {
      entity = invoke.create(entity);
      return entity == null
          ? CommonResponse.fail("记录已存在")
          : entity.getId() > 0 ? CommonResponse.ok(null) : CommonResponse.fail("添加失败");
    } catch (Exception e) {
      log.warn(" CommonServiceImpl.create : [{}]", e.getMessage());
      return CommonResponse.fail("添加失败,数据格式异常");
    }
  }

  @Override
  public synchronized <T extends BaseService<E, U>, E extends AbstractEntity, U> String update(
      T invoke, final String idStr, final String page, Map<String, Object> map, String propName) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) {
      throw new CommonException("id 格式不正确");
    }
    map.put(propName, invoke.getPoJoById(id));
    return page;
  }

  @Override
  public synchronized <T extends BaseService<E, U>, E extends AbstractEntity, U>
      ResponseEntity update(T invoke, E entity, String idStr, BindingResult bindingResult) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    if (bindingResult.hasErrors()) {
      return CommonResponse.fail(collectBindingResult(bindingResult));
    }
    entity.setId(id);
    try {
      entity = invoke.update(entity);
      return entity == null
          ? CommonResponse.fail("记录不存在")
          : entity.getId() > 0 ? CommonResponse.ok(null, "更新成功") : CommonResponse.fail("更新失败");
    } catch (Exception e) {
      log.warn(" CommonServiceImpl.update : [{}]", e.getMessage());
      return CommonResponse.fail("更新失败,数据格式异常");
    }
  }

  @Override
  public synchronized <T extends BaseService<E, U>, E extends AbstractEntity, U>
      ResponseEntity delete(final T invoke, final String idStr) {
    long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) return CommonResponse.wrongFormat("id");
    try {
      invoke.delete(id);
    } catch (Exception e) {
      log.warn(" ConfigController.delete : [{}]", e.getMessage());
      return CommonResponse.fail("删除失败,数据格式异常");
    }
    return CommonResponse.ok(null, "删除成功");
  }

  @Override
  public String collectBindingResult(BindingResult bindingResult) {
    StringBuilder errorMsg = new StringBuilder("提交信息有误: ");
    bindingResult
        .getAllErrors()
        .forEach(
            error ->
                errorMsg
                    .append("\n")
                    .append(
                        Objects.requireNonNull(error.getDefaultMessage())
                                .contains("Failed to convert property")
                            ? ((FieldError) error).getField() + " 格式不正确"
                            : error.getDefaultMessage()));
    return errorMsg.toString();
  }
}
