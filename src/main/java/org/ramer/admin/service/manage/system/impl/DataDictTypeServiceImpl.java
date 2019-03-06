package org.ramer.admin.service.manage.system.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.DataDictType;
import org.ramer.admin.repository.manage.DataDictTypeRepository;
import org.ramer.admin.service.manage.system.DataDictTypeService;
import org.ramer.admin.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictTypeServiceImpl implements DataDictTypeService {
  @Resource private DataDictTypeRepository repository;

  @Transactional
  @Override
  public synchronized DataDictType create(DataDictType dataDictType) {
    textFilter(dataDictType, dataDictType);
    return repository.saveAndFlush(dataDictType);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public DataDictType getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<DataDictType> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<DataDictType> page(final String criteria, final int page, final int size) {
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
                    builder.or(
                        builder.like(root.get("name"), "%" + criteria + "%"),
                        builder.like(root.get("code"), "%" + criteria + "%"),
                        builder.like(root.get("remark"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized DataDictType update(DataDictType dataDictType) {
    return repository
        .findById(dataDictType.getId())
        .map(
            dictType -> {
              textFilter(dataDictType, dictType);
              return repository.saveAndFlush(dictType);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(final long dicId) {
    repository.deleteById(dicId);
  }

  @Override
  public void textFilter(DataDictType trans, DataDictType filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    if (!StringUtils.isEmpty(trans.getCode())) {
      filtered.setCode(TextUtil.filter(trans.getCode()));
    }
  }
}
