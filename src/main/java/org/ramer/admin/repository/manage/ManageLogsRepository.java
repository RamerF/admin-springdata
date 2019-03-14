package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.domain.manage.ManageLog;
import org.ramer.admin.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageLogsRepository extends BaseRepository<ManageLog, Long> {}
