package org.ramer.admin.service.manage.system;

import java.util.List;
import org.ramer.admin.entity.domain.manage.Privilege;
import org.ramer.admin.entity.pojo.manage.PrivilegePoJo;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.service.common.BaseService;

/** @author ramer */
public interface PrivilegeService extends BaseService<Privilege, PrivilegePoJo> {
  /**
   * 创建给定字符串的权限.
   *
   * @param expPrefix eg: config,用于编码权限表达式
   * @param remark eg:系统配置,用于权限配置显示
   * @return 已创建权限集合
   */
  List<Privilege> create(String expPrefix, String remark) throws CommonException;

  List<Privilege> listByManagerId(long personId);

  boolean saveBatch(List<Privilege> privileges);

  List<Privilege> listByRoles(Long rolesId);
}
