package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

/** 系统菜单（此表是系统定义好的，没有增删改操作） */
@Data
@Entity(name = "menu")
@Table
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Menu extends AbstractEntity {
  @ManyToOne
  @JoinColumn(name = "pid")
  @JsonBackReference
  @Where(clause = "state = " + Constant.STATE_ON)
  private Menu parent;
  /** 是否最终节点 */
  private Boolean leaf;

  @Column(length = 25)
  private String name;
  /** 菜单别名,用于权限表达式 */
  @Column(nullable = false, length = 100)
  private String alia;

  @Column(length = 100)
  private String url;
  /** 显示顺序 */
  @Column(length = 2)
  private Integer sort;
  /** ICON FONT 图标 */
  @Column(length = 25)
  private String icon;

  @Column(length = 100)
  private String remark;
  //    @JoinColumn(name = "menu_id")
  //    @OneToMany(fetch = FetchType.EAGER)
  //    private List<Privilege> privileges;

  public static Menu of(long id) {
    final Menu menu = new Menu();
    menu.setId(id);
    return menu;
  }
}
