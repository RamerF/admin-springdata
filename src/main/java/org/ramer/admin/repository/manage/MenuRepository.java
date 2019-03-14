package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import java.util.List;
import org.ramer.admin.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Long> {

  @Query(
      "select menu from org.ramer.admin.entity.domain.manage.Manager m inner join m.roleses r inner join r.menus menu where m.id= :managerId and menu.state= :state order by menu.sort asc")
  List<Menu> findByManager(@Param("managerId") long managerId, @Param("state") int state);

  @Query(
      "select new org.ramer.admin.entity.pojo.manage.MenuPoJo(menu.id,menu.state,menu.name,menu.url,menu.leaf,menu.icon,menu.parent.id,menu.sort,menu.createTime,menu.updateTime) from org.ramer.admin.entity.domain.manage.Manager m inner join m.roleses r inner join r.menus menu where m.id= :managerId and menu.state= :state order by menu.sort asc")
  List<MenuPoJo> findNameByManager(@Param("managerId") Long managerId, @Param("state") int state);
}
