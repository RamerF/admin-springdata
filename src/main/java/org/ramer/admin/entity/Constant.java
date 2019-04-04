package org.ramer.admin.entity;

import java.util.*;
import lombok.AllArgsConstructor;

/**
 * 系统常量定义.
 *
 * @author ramer
 */
@SuppressWarnings({"unused"})
public class Constant {

  public static final String DEFAULT_CSRF_TOKEN = "X-CSRF-TOKEN";
  // 分页每页默认条数
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final String SITE_TITLE = "SITE_TITLE";
  public static final String SITE_NAME = "SITE_NAME";
  public static final String SITE_COPYRIGHT = "SITE_COPYRIGHT";
  public static final String DEFAULT_CHARSET_ENCODE = "UTF-8";

  // 三方调用返回结果码
  public enum ResultCode {
    E0000("成功"),
    E0001("认证码错误"),
    E0002("认证码已被禁用"),
    E0003("认证失败"),
    E0004("参数错误[code,signed,data不能为空]");
    private String desc;

    ResultCode(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return desc;
    }
  }

  /** 可用状态 */
  public static final int STATE_ON = 1;
  /** 不可用状态 */
  public static final int STATE_OFF = -1;

  /** 可登录 */
  public static final int ACTIVE_TRUE = 1;
  /** 不可登录 */
  public static final int ACTIVE_FALSE = 0;

  /** 接口路径 */
  public class AccessPath {
    public static final String USER = "/user";
    public static final String MANAGE = "/manage";
  }

  // 静态资源路径
  public class ResourcePath {
    // 管理端
    public static final String MANAGER = "/manager";
    // 用户端
    public static final String USER = "/user";
    // 用户端
    public static final String PUBLIC = "/public";
    // 默认
    public static final String DEFAULT = "/default";
  }
  /**
   *
   *
   * <pre>
   *     性别:
   *     0: 男
   *     1: 女
   * </pre>
   */
  public enum Gender {
    MALE("男"),
    FEMALE("女");
    private String desc;

    Gender(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return this.desc;
    }

    public static List<Integer> ordinalList() {
      List<Integer> ordinals = new ArrayList<>();
      for (Gender gender : values()) {
        ordinals.add(gender.ordinal());
      }
      return ordinals;
    }
  }
  /** 权限. */
  public enum PrivilegeEnum {
    READ("read", "读"),
    CREATE("create", "增"),
    WRITE("write", "写"),
    DELETE("delete", "删"),
    ALL("*", "所有");
    public String name;
    public String remark;
    /** 全局 */
    public static PrivilegeEnumEntity GLOBAL = new PrivilegeEnumEntity("global", "全局");
    /** 管理端 */
    public static PrivilegeEnumEntity MANAGE = new PrivilegeEnumEntity("manage", "管理端");
    /** 用户端 */
    public static PrivilegeEnumEntity USER = new PrivilegeEnumEntity("user", "用户端");
    /** 认证对象 */
    @AllArgsConstructor
    public static class PrivilegeEnumEntity {
      public String name;
      public String remark;
    }

    PrivilegeEnum(String name, String remark) {
      this.name = name;
      this.remark = remark;
    }

    public static Map<String, String> map() {
      Map<String, String> map = new LinkedHashMap<>();
      for (PrivilegeEnum privilegeEnum : values()) {
        map.put(privilegeEnum.name, privilegeEnum.remark);
      }
      return map;
    }
  }

  /** 数据库字典类型CODE */
  public static class DataDictTypeCode {
    // 用于演示CODE
    public static final String DEMO_CODE = "DEMO_CODE";
  }

  /** 通用文本 */
  public static class Txt {
    // 操作成功提示文本
    public static final String SUCCESS_EXEC = "操作成功";
    public static final String SUCCESS_EXEC_ADD = "添加成功";
    public static final String SUCCESS_EXEC_UPDATE = "更新成功";
    public static final String SUCCESS_EXEC_DELETE = "删除成功";
    // 操作失败提示文本
    public static final String FAIL_EXEC = "操作失败,数据格式有误";
    public static final String FAIL_EXEC_ADD = "添加失败,数据格式有误";
    public static final String FAIL_EXEC_ADD_EXIST = "添加失败,数据已存在";
    public static final String FAIL_EXEC_UPDATE = "更新失败,数据格式有误";
    public static final String FAIL_EXEC_UPDATE_NOT_EXIST = "更新失败,记录不存在";
    public static final String FAIL_EXEC_DELETE_NOT_EXIST = "删除失败";
    // 系统异常提示文本
    public static final String ERROR_PARAM = "参数错误";
    public static final String ERROR_SYSTEM = "系统繁忙，请稍后再试 ！";
    public static final String NOT_IMPLEMENT = "方法未实现";
  }
  /** 存储在session中的key */
  public class SessionKey {
    // 前端登录对象
    public static final String LOGIN_USER = "user";
    // 管理端
    public static final String LOGIN_MANAGER = "manager";
  }
}
