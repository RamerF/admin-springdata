package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.domain.manage.Manager;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {
  Manager findByIdAndState(Long id, int state);

  Manager findByEmpNo(String empNo);
}
