package org.ramer.admin.service.manage.system.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.ManageLog;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.repository.manage.ManageLogsRepository;
import org.ramer.admin.service.manage.system.ManageLogService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ManageLogServiceImpl implements ManageLogService {
  @Resource private ManageLogsRepository repository;

  @Transactional
  @Override
  public synchronized ManageLog create(ManageLog manageLogs) {
    return repository.saveAndFlush(manageLogs);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public ManageLog getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<ManageLog> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<ManageLog> page(String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    if (pageable == null) {
      return new PageImpl<>(Collections.emptyList());
    }
    return StringUtils.isEmpty(criteria)
        ? repository.findAll(pageable)
        : repository.findAll(
            (root, query, builder) ->
                builder.and(
                    builder.equal(root.get("state"), Constant.STATE_ON),
                    builder.or(
                        builder.like(root.get("url"), "%" + criteria + "%"),
                        builder.like(root.get("result"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized ManageLog update(ManageLog manageLogs) {
    log.warn(" ManageLogsServiceImpl.update : not allowed");
    return null;
  }

  @Transactional
  @Override
  public synchronized void delete(final long id) {
    log.error(" ManageLogsServiceImpl.delete : not allowed");
    throw new CommonException("ManageLogsServiceImpl.delete : not allowed");
  }
}
