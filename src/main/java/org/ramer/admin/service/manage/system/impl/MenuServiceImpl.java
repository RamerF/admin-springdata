package org.ramer.admin.service.manage.system.impl;

import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.repository.manage.MenuRepository;
import org.ramer.admin.service.manage.system.MenuService;
import org.ramer.admin.service.manage.system.PrivilegeService;
import org.ramer.admin.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
  @Resource private MenuRepository repository;
  @Resource private PrivilegeService privilegeService;

  @Override
  public List<Menu> listByManager(Long managerId) {
    return repository.findByManager(managerId, Constant.STATE_ON);
  }

  @Override
  public List<MenuPoJo> listNameByManager(Long managerId) {
    return repository.findNameByManager(managerId, Constant.STATE_ON);
  }

  @Transactional
  @Override
  public Menu create(Menu menu) throws CommonException {
    textFilter(menu, menu);
    privilegeService.create(menu.getAlia(), menu.getRemark());
    return repository.saveAndFlush(menu);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public Menu getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Menu> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<Menu> page(final String criteria, final int page, final int size) {
    final PageRequest pageable = pageRequest(page, size);
    if (pageable == null) {
      new PageImpl<>(Collections.emptyList());
    }
    return StringUtils.isEmpty(criteria)
        ? repository.findAll(pageable)
        : repository.findAll(
            (root, query, builder) ->
                builder.and(
                    builder.equal(root.get("state"), Constant.STATE_ON),
                    builder.and(
                        builder.like(root.get("name"), "%" + criteria + "%"),
                        builder.like(root.get("remark"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized Menu update(Menu m) {
    return Optional.ofNullable(getById(m.getId()))
        .map(
            menu -> {
              textFilter(m, menu);
              menu.setLeaf(m.getLeaf());
              menu.setSort(m.getSort());
              menu.setParent(m.getParent());
              return repository.saveAndFlush(menu);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(Menu trans, Menu filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    if (!StringUtils.isEmpty(trans.getIcon())) {
      filtered.setIcon(TextUtil.filter(trans.getIcon()));
    }
    filtered.setUrl(TextUtil.filter(trans.getUrl()));
  }
}
