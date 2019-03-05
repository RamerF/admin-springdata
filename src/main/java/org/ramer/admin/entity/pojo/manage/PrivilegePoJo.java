package org.ramer.admin.entity.pojo.manage;

import lombok.*;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** 系统数据字典 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PrivilegePoJo extends AbstractEntityPoJo {
  /** 权限表达式. eg: global:view */
  private String exp;

  private String remark;
}
