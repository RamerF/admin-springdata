package org.ramer.admin.entity.pojo.manage;

import lombok.*;
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
}
