package org.ramer.admin.entity.pojo.manage;

import lombok.*;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** 系统数据字典. */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictTypePoJo extends AbstractEntityPoJo {
  private String code;
  private String name;
  private String remark;
}
