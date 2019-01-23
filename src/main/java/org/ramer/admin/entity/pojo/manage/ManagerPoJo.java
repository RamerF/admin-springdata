package org.ramer.admin.entity.pojo.manage;

import java.util.Date;
import java.util.List;

import lombok.*;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ManagerPoJo {
  private Long id;
  private String name;
  private String gender;
  private String empNo;
  private Boolean avatar;
  private String phone;
  private List<String> roles;
  private Date validDate;
  private Boolean active;
}
