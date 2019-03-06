package org.ramer.admin.service.manage.system.impl;

import java.util.List;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.thirdparty.ThirdPartyCertificate;
import org.ramer.admin.entity.domain.thirdparty.ThirdPartyLog;
import org.ramer.admin.repository.thirdparty.ThirdPartyCertRepository;
import org.ramer.admin.repository.thirdparty.ThirdPartyLogRepository;
import org.ramer.admin.service.ThirdPartyCertService;
import org.ramer.admin.util.TextUtil;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Service
@Slf4j
public class ThirdPartyCertServiceImpl implements ThirdPartyCertService {
  @Resource private ThirdPartyCertRepository repository;
  @Resource private ThirdPartyLogRepository logRepository;

  @Transactional
  @Override
  public ThirdPartyLog saveLog(ThirdPartyLog thirdPartyLog) {
    return logRepository.saveAndFlush(thirdPartyLog);
  }

  @Override
  public ThirdPartyCertificate findByCode(String code) {
    return repository.findByCode(code);
  }

  @Transactional
  @Override
  public synchronized ThirdPartyCertificate create(ThirdPartyCertificate thirdPartyCertificate) {
    textFilter(thirdPartyCertificate, thirdPartyCertificate);
    return repository.saveAndFlush(thirdPartyCertificate);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public ThirdPartyCertificate getById(final long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<ThirdPartyCertificate> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<ThirdPartyCertificate> page(String criteria, final int page, final int size) {
    final PageRequest pageable = PageRequest.of(page - 1, size);
    return StringUtils.isEmpty(criteria)
        ? repository.findAll(pageable)
        : repository.findAll(
            StringUtils.isEmpty(criteria)
                ? (root, query, builder) ->
                    builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
                : (root, query, builder) ->
                    builder.and(
                        builder.equal(root.get("state"), Constant.STATE_ON),
                        builder.or(
                            builder.like(root.get("name"), "%" + criteria + "%"),
                            builder.like(root.get("remark"), "%" + criteria + "%"))),
            pageable);
  }

  @Transactional
  @Override
  public synchronized ThirdPartyCertificate update(ThirdPartyCertificate thirdPartyCertificate) {
    return repository
        .findById(thirdPartyCertificate.getId())
        .map(
            partyCertificate -> {
              textFilter(thirdPartyCertificate, partyCertificate);
              return repository.saveAndFlush(partyCertificate);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(final long id) {
    repository.deleteById(id);
  }

  @Override
  public void textFilter(final ThirdPartyCertificate trans, final ThirdPartyCertificate filtered) {
    filtered.setName(TextUtil.filter(trans.getName()));
    filtered.setRemark(TextUtil.filter(trans.getRemark()));
    filtered.setCode(TextUtil.filter(trans.getCode()));
    filtered.setSecret(TextUtil.filter(trans.getSecret()));
  }
}
