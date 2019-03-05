package org.ramer.admin.entity.pojo.manage;

import lombok.*;
import org.ramer.admin.entity.domain.manage.Config;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ConfigPoJo extends AbstractEntityPoJo {
  private String code;
  private String name;
  private String value;
  private String remark;

  public static ConfigPoJo of(Config config) {
    ConfigPoJo poJo = new ConfigPoJo();
    poJo.setCode(config.getCode());
    poJo.setName(config.getName());
    poJo.setValue(config.getValue());
    poJo.setRemark(config.getRemark());

    AbstractEntityPoJo.of(poJo, config);
    return poJo;
  }
}
