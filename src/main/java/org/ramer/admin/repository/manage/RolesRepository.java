package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.domain.manage.Roles;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends BaseRepository<Roles, Long> {
  @Query(
      "select r from org.ramer.admin.entity.domain.manage.Manager m inner join m.roleses r where m.id= :managerId and r.state= :state")
  List<Roles> findByManager(@Param("managerId") long managerId, @Param("state") int state);

  @Query(
      "select r.name from org.ramer.admin.entity.domain.manage.Manager m inner join m.roleses r where m.id= :managerId and r.state= :state")
  List<String> findNameByManager(@Param("managerId") long managerId, @Param("state") int state);
}
