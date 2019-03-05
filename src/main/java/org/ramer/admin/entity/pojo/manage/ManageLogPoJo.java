package org.ramer.admin.entity.pojo.manage;

import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.domain.manage.ManageLog;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class ManageLogPoJo extends AbstractEntityPoJo {
  private String url;
  private Long managerId;
  private String ip;
  private String result;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ManageLog manageLog = (ManageLog) entity;
    ManageLogPoJo poJo = new ManageLogPoJo();
    if (manageLog.getManager() != null) {
      poJo.setManagerId(manageLog.getManager().getId());
    }
    BeanUtils.copyProperties(manageLog, poJo);
    return (T) poJo;
  }
}
