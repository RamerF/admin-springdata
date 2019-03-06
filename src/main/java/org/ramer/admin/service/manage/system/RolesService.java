package org.ramer.admin.service.manage.system;

import java.util.List;
import org.ramer.admin.entity.domain.manage.Roles;
import org.ramer.admin.entity.pojo.manage.RolesPoJo;
import org.ramer.admin.service.common.BaseService;

public interface RolesService extends BaseService<Roles, RolesPoJo> {

  Roles create(Roles role, List<Long> menuIds, List<Long> privilegeIds);

  Roles update(Roles roles, List<Long> menuIds, List<Long> privilegeIds);

  List<Roles> listByManager(final long managerId);

  List<String> listNameByManager(final long managerId);

  List<Long> listMenuIds(Roles role);
}
