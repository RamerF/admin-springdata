package org.ramer.admin.entity.pojo.manage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ramer.admin.entity.domain.manage.Roles;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** @author ramer created on 11/5/18 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolesPoJo extends AbstractEntityPoJo {
  private String name;
  private String remark;

  public static RolesPoJo of(Roles roles) {
    RolesPoJo poJo = new RolesPoJo();
    poJo.setName(roles.getName());
    poJo.setRemark(roles.getRemark());

    AbstractEntityPoJo.of(poJo, roles);
    return poJo;
  }
}
