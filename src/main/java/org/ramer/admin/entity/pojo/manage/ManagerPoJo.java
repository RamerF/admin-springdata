package org.ramer.admin.entity.pojo.manage;

import java.util.Date;
import lombok.*;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ManagerPoJo extends AbstractEntityPoJo {
  private String empNo;
  private String password;
  private String name;
  private Integer gender;
  private String phone;
  private String avatar;
  private Boolean active;
  private Date validDate;
}
