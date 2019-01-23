package org.ramer.admin.entity.domain.manage;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

/** 系统管理员. */
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
  /** 审核状态 */
  @Column(nullable = false, columnDefinition = "tinyint default 1")
  private Integer active;

  public String getActiveDesc() {
    return active.equals(Constant.ACTIVE_TRUE) ? "已审核" : "未审核";
  }

  @Column(nullable = true)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date validDate;

  @ManyToMany
  @JoinTable(
      name = "manager_roles",
      joinColumns = {@JoinColumn(name = "manager_id")},
      inverseJoinColumns = {@JoinColumn(name = "roles_id")})
  @Where(clause = "state = " + Constant.STATE_ON)
  private List<Roles> roleses;

  public Manager(Long id) {
    setId(id);
  }
}
