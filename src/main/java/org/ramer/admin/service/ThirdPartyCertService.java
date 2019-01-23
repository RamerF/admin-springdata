package org.ramer.admin.service;

import org.ramer.admin.entity.domain.thirdparty.ThirdPartyCertificate;
import org.ramer.admin.entity.domain.thirdparty.ThirdPartyLog;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface ThirdPartyCertService {
  ThirdPartyLog saveLog(ThirdPartyLog log);

  ThirdPartyCertificate findByCode(String code);

  ThirdPartyCertificate save(String code, String secret, String name, String remark);

  Page<ThirdPartyCertificate> page(String name, Integer page, Integer size);

  ThirdPartyCertificate update(Long id, String code, String secret, String name, String remark);

  void delete(Long id);
}
