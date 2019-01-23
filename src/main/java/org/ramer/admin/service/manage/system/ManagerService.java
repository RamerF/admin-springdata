package org.ramer.admin.service.manage.system;

import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.service.common.BaseService;
import org.ramer.admin.service.manage.system.impl.ManagerServiceImpl.ManagerLogin;
import java.util.List;

public interface ManagerService extends BaseService<Manager, Manager> {

  Manager save(Manager o, List<Long> roleIds);

  Manager update(Manager o, List<Long> roleIds);

  Manager getByEmpNo(String empNo);

  /**
   * 更新管理员密码.
   *
   * @return
   *     <pre>
   * 1: 成功<br>
   * 0: 失败<br>
   * -1: 密码不匹配<br>
   * -2: 管理员不存在<br>
   * </pre>
   */
  int updatePassword(Long id, String old, String password);

  ManagerLogin getLoginStatus(String empNo);

  void setLoginStatus(String empNo);
}
