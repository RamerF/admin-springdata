package org.ramer.admin.service.manage.system;

import org.ramer.admin.entity.domain.manage.Privilege;
import org.ramer.admin.service.common.BaseService;
import java.util.List;

/** @author ramer */
public interface PrivilegeService extends BaseService<Privilege, Privilege> {
  /**
   * 创建给定字符串的权限.
   *
   * @param expPrefix eg: config,用于编码权限表达式
   * @param remark eg:系统配置,用于权限配置显示
   * @return 已创建权限集合
   */
  List<Privilege> create(String expPrefix, String remark) throws Exception;

  List<Privilege> listByManagerId(long personId);

  boolean saveBatch(List<Privilege> privileges);

  List<Privilege> listByRoles(Long rolesId);
}
