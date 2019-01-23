package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.AbstractEntity;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Where;

/** 系统操作员角色. */
@Entity(name = "roles")
@Table
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Roles extends AbstractEntity {
  @Column(nullable = false, length = 20)
  private String name;

  @Column(length = 100)
  private String remark;

  @Where(clause = "state = " + Constant.STATE_ON)
  @ManyToMany
  @JoinTable(
      name = "roles_menu",
      joinColumns = {@JoinColumn(name = "roles_id")},
      inverseJoinColumns = {@JoinColumn(name = "menu_id")})
  @OrderBy(value = "id")
  private List<Menu> menus;

  @Column
  @ManyToMany
  @JoinTable(
      name = "roles_privileges",
      joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
  private List<Privilege> privileges;

  private Roles(long id) {
    setId(id);
  }

  public Roles(String name) {
    this.name = name;
  }

  public static Roles of(long id) {
    return new Roles(id);
  }
}
