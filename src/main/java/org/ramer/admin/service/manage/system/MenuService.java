package org.ramer.admin.service.manage.system;

import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import org.ramer.admin.service.common.BaseService;
import java.util.List;

/** @author ramer */
public interface MenuService extends BaseService<Menu, MenuPoJo> {

  List<Menu> listByManager(final long managerId);

  List<MenuPoJo> listNameByManager(final long managerId);
}
