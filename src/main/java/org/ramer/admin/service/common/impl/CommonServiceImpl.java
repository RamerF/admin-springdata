package org.ramer.admin.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.entity.response.manage.MenuResponse;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.service.common.BaseService;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.ConfigService;
import org.ramer.admin.service.manage.system.MenuService;
import org.ramer.admin.util.TextUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    menuPoJos.forEach(menuPoJo -> menusAll.add(MenuResponse.of(menuPoJo)));
    final List<MenuResponse> menus =
        menuPoJos.stream()
            .filter(menuPoJo -> menuPoJo.getPId() == null)
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    menusAll.removeAll(menus);
    Stack<MenuResponse> retain = new Stack<>();
    menus.forEach(retain::push);
    while (retain.size() > 0 && menusAll.size() > 0) {
      MenuResponse menu = retain.pop();
      // 当前节点的子节点
      List<MenuResponse> child =
          menusAll.stream()
              .filter(menuResponse -> menuResponse.getPId().equals(menu.getId()))
              .collect(Collectors.toList());
      // 子节点具有叶子节点,入栈
      child.stream().filter(menuResponse -> !menuResponse.getLeaf()).forEach(retain::push);
      menu.setChildren(child);
      menusAll.removeAll(child);
    }
    map.put("menus", menus);
    JSONObject siteJson = new JSONObject();
    siteJson.put("title", configService.getSiteInfo(Constant.SITE_TITLE));
    siteJson.put("name", configService.getSiteInfo(Constant.SITE_NAME));
    siteJson.put("copyright", configService.getSiteInfo(Constant.SITE_COPYRIGHT));
    map.put("site", siteJson);
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity create(S invoke, T entity, BindingResult bindingResult) {
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
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          final String idStr,
          final String page,
          Map<String, Object> map,
          String propName) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) {
      throw new CommonException("id 格式不正确");
    }
    map.put(propName, invoke.getPoJoById(id, clazz));
    return page;
  }

  @Override
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity update(S invoke, T entity, String idStr, BindingResult bindingResult) {
    final long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) {
      return CommonResponse.wrongFormat("id");
    }
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
  public <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity delete(final S invoke, final String idStr) {
    long id = TextUtil.validLong(idStr, 0);
    if (id <= 0) {
      return CommonResponse.wrongFormat("id");
    }
    try {
      invoke.delete(id);
    } catch (Exception e) {
      log.warn(" ConfigController.delete : [{}]", e.getMessage());
      return CommonResponse.fail("删除失败,数据格式异常");
    }
    return CommonResponse.ok(null, "删除成功");
  }

  @Override
  public <T extends AbstractEntity> ResponseEntity page(
      final Page<T> page, final Function<T, ?> function) {
    return CommonResponse.ok(
        new PageImpl<>(
            page.getContent().stream().map(function).collect(Collectors.toList()),
            page.getPageable(),
            page.getTotalElements()));
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
