package org.ramer.admin.config;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.Constant.Gender;
import org.ramer.admin.entity.Constant.PrivilegeEnum;
import org.ramer.admin.entity.domain.manage.*;
import org.ramer.admin.service.manage.system.*;
import org.ramer.admin.util.EncryptUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Run once after the project start.
 *
 * @author Ramer
 */
@Slf4j
@Component
public class ApplicationInit implements ApplicationRunner {
  @Resource private ManagerService managerService;
  @Resource private RolesService rolesService;
  @Resource private PrivilegeService privilegeService;
  @Resource private MenuService menuService;
  @Resource private ConfigService configService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (rolesService.count() < 1) {
      log.debug("onApplicationEvent ：init security");
      // the access privileges for  super admin default
      Roles roles = new Roles();
      roles.setName("超级管理员");
      roles.setRemark("超级管理员");
      Privilege globalPrivilege = new Privilege();
      globalPrivilege.setExp(PrivilegeEnum.GLOBAL.name + ":" + PrivilegeEnum.ALL.name);
      globalPrivilege.setRemark(PrivilegeEnum.GLOBAL.remark + ":" + PrivilegeEnum.ALL.remark);
      List<Privilege> privileges = new ArrayList<>();
      privileges.add(globalPrivilege);
      roles.setPrivileges(privileges);
      roles.setMenus(menuService.list(null));
      privilegeService.saveBatch(privileges);
      rolesService.create(roles);
      // 初始化超级管理员
      Manager manager = managerService.getByEmpNo("admin");
      if (manager == null) {
        manager = new Manager();
        manager.setEmpNo("admin");
        manager.setActive(1);
        manager.setPhone("84255885");
        manager.setGender(Gender.MALE.ordinal());
        manager.setState(Constant.STATE_ON);
        manager.setCreateTime(new Date());
        manager.setUpdateTime(new Date());
        manager.setPassword(EncryptUtil.execEncrypt("admin"));
        manager.setName("admin");
        manager.setValidDate(
            Date.from(LocalDateTime.now().plusYears(100).toInstant(ZoneOffset.of("+8"))));
        manager.setRoleses(Collections.singletonList(roles));
        managerService.create(manager);

        // 初始化系统参数
        configService.create(new Config("SITE_TITLE", "后台管理系统框架", "后台管理系统框架", "网站标题"));
        configService.create(new Config("SITE_NAME", "XXX管理系统", "XXX管理系统", "系统名称"));
        configService.create(
            new Config(
                "SITE_COPYRIGHT",
                "copyright",
                "Copyright ©2018 ramer v1.0 All Rights Reserved",
                "注册信息"));

        // 初始化菜单
        Menu systemMenu =
            menuService.create(new Menu(null, false, "系统", "system", null, 2, "fa-cog", "系统"));
        Menu configMenu =
            menuService.create(
                new Menu(
                    systemMenu,
                    true,
                    "参数配置",
                    "config",
                    "/manage/config/index",
                    1,
                    "fa-cog",
                    "参数配置"));
        Menu dataDictMenu =
            menuService.create(
                new Menu(
                    systemMenu,
                    true,
                    "数据字典",
                    "dataDict",
                    "/manage/dataDict/index",
                    2,
                    "fa-cog",
                    "数据字典"));
        Menu managerMenu =
            menuService.create(
                new Menu(
                    systemMenu,
                    true,
                    "管理员管理",
                    "manager",
                    "/manage/manager/index",
                    3,
                    "fa-cog",
                    "管理员管理"));
        Menu rolesMenu =
            menuService.create(
                new Menu(
                    systemMenu, true, "角色管理", "roles", "/manage/roles/index", 4, "fa-cog", "角色管理"));
        Menu menuMenu =
            menuService.create(
                new Menu(
                    systemMenu, true, "菜单管理", "menu", "/manage/menu/index", 5, "fa-cog", "菜单管理"));

        // 初始化角色菜单关系表
        ArrayList<Menu> menus = new ArrayList<>();
        menus.add(systemMenu);
        menus.add(configMenu);
        menus.add(dataDictMenu);
        menus.add(managerMenu);
        menus.add(rolesMenu);
        menus.add(menuMenu);
        Roles superAdmin = rolesService.getById(1);
        superAdmin.setMenus(menus);
        rolesService.create(superAdmin);
      }
      // user role
      roles = new Roles();
      roles.setName("普通用户");
      roles.setRemark("普通用户");
      privileges = new ArrayList<>();
      Privilege userPrivilege = new Privilege();
      // the access privileges for user default
      userPrivilege.setExp(PrivilegeEnum.USER.name + ":" + PrivilegeEnum.ALL.name);
      userPrivilege.setRemark(PrivilegeEnum.USER.remark + ":" + PrivilegeEnum.ALL.remark);
      privileges.add(userPrivilege);
      roles.setPrivileges(privileges);
      privilegeService.saveBatch(privileges);
      rolesService.create(roles);
    }
  }
}
