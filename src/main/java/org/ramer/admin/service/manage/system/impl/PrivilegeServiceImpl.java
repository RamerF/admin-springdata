package org.ramer.admin.service.manage.system.impl;

import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.Constant.PrivilegeEnum;
import org.ramer.admin.entity.domain.manage.Privilege;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.repository.manage.PrivilegeRepository;
import org.ramer.admin.service.manage.system.PrivilegeService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeService {
  @Resource private PrivilegeRepository repository;

  @Transactional
  @Override
  public List<Privilege> create(final String expPrefix, final String remark) {
    List<Privilege> privileges = new ArrayList<>();
    PrivilegeEnum.map()
        .forEach(
            (name, remark2) -> {
              final Privilege p = new Privilege();
              p.setExp(expPrefix + ":" + name);
              p.setRemark(remark + ":" + remark2);
              privileges.add(create(p));
            });
    return privileges;
  }

  @Override
  public List<Privilege> listByManagerId(final long managerId) {
    return repository.findByManager(managerId, Constant.STATE_ON);
  }

  @Transactional
  @Override
  public synchronized boolean saveBatch(List<Privilege> privileges) {
    return repository.saveAll(privileges).size() > 0;
  }

  @Override
  public List<Privilege> listByRoles(final long rolesId) {
    return repository.findByRoles(rolesId, Constant.STATE_ON);
  }

  @Transactional
  @Override
  public Privilege create(Privilege privilege) {
    return repository.save(privilege);
  }

  @Override
  public long count() {
    log.error(" PrivilegeServiceImpl.count : not yet implements");
    throw new CommonException("PrivilegeServiceImpl.count : not yet implements");
  }

  @Override
  public Privilege getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Privilege> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Privilege> page(final String criteria, final int page, final int size) {
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
                        builder.like(root.get("exp"), "%" + criteria + "%"),
                        builder.like(root.get("remark"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized Privilege update(Privilege privilege) {
    log.error(" PrivilegeServiceImpl.update : not yet implements");
    throw new CommonException("PrivilegeServiceImpl.update : not yet implements");
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }
}
