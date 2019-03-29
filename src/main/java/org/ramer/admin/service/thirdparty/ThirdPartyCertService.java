package org.ramer.admin.service.thirdparty;

import org.ramer.admin.entity.domain.thirdparty.ThirdPartyCertificate;
import org.ramer.admin.entity.domain.thirdparty.ThirdPartyLog;
import org.ramer.admin.entity.pojo.thirdparty.ThirdPartyCertificatePoJo;
import org.ramer.admin.service.common.BaseService;

/** @author ramer */
public interface ThirdPartyCertService
    extends BaseService<ThirdPartyCertificate, ThirdPartyCertificatePoJo> {
  ThirdPartyLog saveLog(ThirdPartyLog log);

  ThirdPartyCertificate findByCode(String code);
}
