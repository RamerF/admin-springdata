package org.ramer.admin.entity.pojo.manage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ramer.admin.entity.domain.manage.ManageLog;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

@Data
@EqualsAndHashCode(callSuper = true)
public class ManageLogPoJo extends AbstractEntityPoJo {
  private String url;
  private Long managerId;
  private String ip;
  private String result;

  public static ManageLogPoJo of(ManageLog log) {
    if (log == null) {
      return null;
    }
    ManageLogPoJo poJo = new ManageLogPoJo();
    poJo.setUrl(log.getUrl());
    if (log.getManager() != null) {
      poJo.setManagerId(log.getManager().getId());
    }
    poJo.setIp(log.getIp());
    poJo.setResult(log.getResult());

    AbstractEntityPoJo.of(poJo, log);
    return poJo;
  }
}
