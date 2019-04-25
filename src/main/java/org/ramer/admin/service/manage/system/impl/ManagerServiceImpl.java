package org.ramer.admin.service.manage.system.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.entity.domain.manage.Roles;
import org.ramer.admin.repository.manage.ManagerRepository;
import org.ramer.admin.service.manage.system.ManagerService;
import org.ramer.admin.util.EncryptUtil;
import org.ramer.admin.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {
  @Resource private ManagerRepository repository;
  private static Map<String, ManagerLogin> LOGIN_STATUS_MAP = new HashMap<>();

  @Transactional
  @Override
  public synchronized Manager save(Manager manager, List<Long> roleIds) {
    if (roleIds != null && roleIds.size() > 0) {
      List<Roles> roles = new ArrayList<>();
      roleIds.forEach(roleId -> roles.add(Roles.of(roleId)));
      manager.setRoleses(roles);
      manager.setPassword(EncryptUtil.execEncrypt(manager.getPassword()));
    }
    return create(manager);
  }

  @Override
  public Manager getByEmpNo(String empNo) {
    return repository.findByEmpNo(empNo);
  }

  @Transactional
  @Override
  public synchronized Manager update(Manager manager, List<Long> roleIds) {
    // TODO-WARN: 调用update方法
    Manager m = repository.findByIdAndState(manager.getId(), Constant.STATE_ON);
    if (m == null) {
      return null;
    }
    if (!StringUtils.isEmpty(manager.getPassword())) {
      m.setPassword(manager.getPassword());
    }
    if (!StringUtils.isEmpty(manager.getPhone())) {
      m.setPhone(manager.getPhone());
    }
    textFilter(manager, m);
    if (manager.getActive() != null
        && (manager.getActive() == Constant.ACTIVE_TRUE
            || manager.getActive() == Constant.ACTIVE_FALSE)) {
      m.setActive(manager.getActive());
    }
    if (Constant.Gender.ordinalList().contains(manager.getGender())) {
      m.setGender(manager.getGender());
    }
    if (roleIds != null && roleIds.size() > 0) {
      List<Roles> roles = new ArrayList<>();
      roleIds.forEach(roleId -> roles.add(Roles.of(roleId)));
      m.setRoleses(roles);
    }
    return repository.saveAndFlush(m);
  }

  @Transactional
  @Override
  public synchronized int updatePassword(final long id, String old, String password) {
    Optional<Manager> optionalManager = repository.findById(id);
    if (optionalManager.isPresent()) {
      Manager person = optionalManager.get();
      if (!EncryptUtil.matches(old, person.getPassword())) {
        return -1;
      }
      person.setPassword(EncryptUtil.execEncrypt(password));
      Manager manager = repository.saveAndFlush(person);
      return manager != null && manager.getId() > 0 ? 1 : 0;
    } else {
      return -2;
    }
  }

  @Override
  public synchronized ManagerLogin getLoginStatus(String empNo) {
    final ManagerLogin managerLogin = LOGIN_STATUS_MAP.get(empNo);
    if (managerLogin != null
        && new Date().getTime() - managerLogin.getDuring() / (24 * 60 * 60 * 1000) >= 1) {
      // 一天后重置
      managerLogin.setDuring(new Date().getTime());
      managerLogin.setCount(0);
    }
    return managerLogin;
  }

  @Override
  public synchronized void setLoginStatus(String empNo) {
    Optional.ofNullable(LOGIN_STATUS_MAP.get(empNo))
        .map(
            s -> {
              s.setCount(s.getCount() + 1);
              LOGIN_STATUS_MAP.put(empNo, s);
              return LOGIN_STATUS_MAP;
            })
        .orElseGet(
            () -> {
              ManagerLogin login = new ManagerLogin();
              login.setCount(1);
              login.setDuring(new Date().getTime());
              LOGIN_STATUS_MAP.put(empNo, login);
              return LOGIN_STATUS_MAP;
            });
  }

  @Transactional
  @Override
  public Manager create(Manager manager) {
    textFilter(manager, manager);
    if (manager.getActive() == null) {
      manager.setActive(Constant.ACTIVE_FALSE);
    }
    if (manager.getState() == null) {
      manager.setState(Constant.STATE_OFF);
    }
    return repository.saveAndFlush(manager);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Manager getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Manager> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Manager> page(final String criteria, final int page, final int size) {
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
                        builder.like(root.get("empNo"), "%" + criteria + "%"),
                        builder.like(root.get("name"), "%" + criteria + "%"),
                        builder.like(root.get("phone"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized Manager update(Manager manager) {
    // TODO-WARN: 这里调用写反了,应该是其他地方调用这个方法
    return update(
        manager,
        Optional.ofNullable(manager.getRoleses()).orElseGet(ArrayList::new).stream()
            .mapToLong(AbstractEntity::getId)
            .boxed()
            .collect(Collectors.toList()));
  }

  @Transactional
  @Override
  public synchronized void delete(final long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(Manager trans, Manager filtered) {
    if (!StringUtils.isEmpty(trans.getEmpNo())) {
      filtered.setEmpNo(TextUtil.filter(trans.getEmpNo()));
    }
    if (!StringUtils.isEmpty(trans.getName())) {
      filtered.setName(TextUtil.filter(trans.getName()));
    }
  }

  @Data
  public static class ManagerLogin {
    private Integer count;
    private long during;
  }
}
