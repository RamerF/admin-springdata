package org.ramer.admin.entity.domain.manage;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 管理员.
 *
 * @author ramer
 */
@Entity(name = "manager")
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Manager extends AbstractEntity {
  @Column(nullable = false, length = 25)
  private String empNo;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false, length = 25)
  private String name;

  @Column(nullable = false)
  private Integer gender;

  @Column(nullable = false, length = 11)
  private String phone;

  @Column(length = 50)
  private String avatar;

  // TODO-TIP: active字段暂时没用
  /** 审核状态 */
  @Column(nullable = false, columnDefinition = "tinyint default 1 comment '审核状态'")
  private Integer active;

  public String getActiveDesc() {
    return active != null && active.equals(Constant.ACTIVE_TRUE) ? "已审核" : "未审核";
  }
  // TODO-TIP: validDate字段暂时没用
  @Column
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date validDate;

  @ManyToMany
  @JoinTable(
      name = "manager_roles",
      joinColumns = {@JoinColumn(name = "manager_id")},
      inverseJoinColumns = {@JoinColumn(name = "roles_id")})
  private List<Roles> roleses;

  public static Manager of(Long id) {
    if (Objects.isNull(id)) {
      return null;
    }
    final Manager manager = new Manager();
    manager.setId(id);
    return manager;
  }
}
