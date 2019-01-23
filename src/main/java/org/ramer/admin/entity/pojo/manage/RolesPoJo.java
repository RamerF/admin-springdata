package org.ramer.admin.entity.pojo.manage;

import java.util.Date;
import lombok.Data;
import org.ramer.admin.entity.domain.manage.Roles;

/** @author ramer created on 11/5/18 */
@Data
public class RolesPoJo {
  private long id;
  private Integer state;
  private String name;
  private String remark;
  private Date createTime;
  private Date updateTime;

  public static RolesPoJo of(Roles roles) {
    if (roles == null) return null;
    RolesPoJo rolesPoJo = new RolesPoJo();
    rolesPoJo.setId(roles.getId());
    rolesPoJo.setState(roles.getState());
    rolesPoJo.setName(roles.getName());
    rolesPoJo.setRemark(roles.getRemark());
    rolesPoJo.setCreateTime(roles.getCreateTime());
    rolesPoJo.setUpdateTime(roles.getUpdateTime());
    return rolesPoJo;
  }
}
