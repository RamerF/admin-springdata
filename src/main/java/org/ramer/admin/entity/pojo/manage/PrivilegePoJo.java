package org.ramer.admin.entity.pojo.manage;

import lombok.*;
import org.ramer.admin.entity.domain.manage.Privilege;
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

  public static PrivilegePoJo of(Privilege privilege) {
    if (privilege == null) {
      return null;
    }
    PrivilegePoJo poJo = new PrivilegePoJo();
    poJo.setExp(privilege.getExp());
    poJo.setRemark(privilege.getRemark());

    AbstractEntityPoJo.of(poJo, privilege);
    return poJo;
  }
}
