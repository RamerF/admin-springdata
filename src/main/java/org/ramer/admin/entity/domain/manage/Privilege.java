package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.*;
import org.ramer.admin.entity.Constant;

/**
 * 系统权限.
 *
 * @author ramer
 * @see Constant.PrivilegeEnum
 */
@Entity(name = "privilege")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Privilege extends AbstractEntity {
  /** 权限表达式. eg: global:read */
  @Column(length = 100)
  private String exp;

  @Column(length = 50)
  private String remark;

  public static Privilege of(long id) {
    final Privilege privilege = new Privilege();
    privilege.setId(id);
    return privilege;
  }
}
