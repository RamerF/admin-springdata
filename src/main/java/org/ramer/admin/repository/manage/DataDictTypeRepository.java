package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.domain.manage.DataDictType;
import org.ramer.admin.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataDictTypeRepository extends BaseRepository<DataDictType, Long> {
  DataDictType findByCode(String code);
}
