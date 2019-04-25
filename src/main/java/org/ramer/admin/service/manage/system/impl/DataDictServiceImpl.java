package org.ramer.admin.service.manage.system.impl;

import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.entity.domain.manage.DataDictType;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.repository.manage.DataDictRepository;
import org.ramer.admin.repository.manage.DataDictTypeRepository;
import org.ramer.admin.service.manage.system.DataDictService;
import org.ramer.admin.util.TextUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class DataDictServiceImpl implements DataDictService {
  @Resource private DataDictTypeRepository dictTypeRepository;
  @Resource private DataDictRepository repository;

  @Override
  public List<DataDictType> listType() {
    return dictTypeRepository.findAll();
  }

  @Override
  public DataDict getByTypeCodeAndCode(String typeCode, String code) {
    //    return repository.findByDataDictTypeCodeAndCodeAndState(
    //        dictTypeRepository.findByCode(typeCode), code, Constant.STATE_ON);
    return repository.findByDataDictTypeCodeAndCodeAndState(typeCode, code, Constant.STATE_ON);
  }

  @Override
  public Page<DataDict> pageByTypeCode(
      String typeCode, String criteria, final int page, final int size) {
    if (page < 1 || size < 0) {
      return new PageImpl<>(Collections.emptyList());
    }
    return StringUtils.isEmpty(criteria)
        ? repository.findByTypeCodeAndState(
            typeCode, Constant.STATE_ON, PageRequest.of(page - 1, size))
        : repository.findByTypeCodeAndState(
            typeCode, "%" + criteria + "%", Constant.STATE_ON, PageRequest.of(page - 1, size));
  }

  @Transactional
  @Override
  public DataDict create(DataDict dataDict, String typeCode) {
    DataDict dict = new DataDict();
    textFilter(dataDict, dict);
    final DataDictType dataDictType = dictTypeRepository.findByCode(typeCode);
    if (dataDictType == null) {
      throw new CommonException("参数typeCode不正确");
    }
    dict.setDataDictType(dataDictType);
    return repository.saveAndFlush(dict);
  }

  @Transactional
  @Override
  public synchronized DataDict create(DataDict dataDict) {
    return create(dataDict, Objects.requireNonNull(dataDict.getDataDictType()).getCode());
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public DataDict getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<DataDict> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<DataDict> page(final String criteria, final int page, final int size) {
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
                        builder.like(root.get("name"), "%" + criteria + "%"),
                        builder.like(root.get("code"), "%" + criteria + "%"),
                        builder.like(root.get("remark"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized DataDict update(DataDict dataDict) {
    return repository
        .findById(dataDict.getId())
        .map(
            dict -> {
              textFilter(dataDict, dict);
              return repository.saveAndFlush(dict);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(final long dicId) {
    repository.deleteById(dicId);
  }

  @Override
  public void textFilter(DataDict trans, DataDict filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    if (!StringUtils.isEmpty(trans.getCode())) {
      filtered.setCode(TextUtil.filter(trans.getCode()));
    }
  }
}
