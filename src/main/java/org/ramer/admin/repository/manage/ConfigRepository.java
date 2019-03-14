package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.domain.manage.Config;
import org.ramer.admin.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends BaseRepository<Config, Long> {
  Config findByCode(String code);

  Config findByCodeAndState(String code, int state);
}
