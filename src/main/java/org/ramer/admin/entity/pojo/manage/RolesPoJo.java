package org.ramer.admin.entity.pojo.manage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** @author ramer created on 11/5/18 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolesPoJo extends AbstractEntityPoJo {
  private String name;
  private String remark;
}
