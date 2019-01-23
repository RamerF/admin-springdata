package org.ramer.admin.entity.pojo.manage;

import java.util.Date;
import lombok.*;
import org.ramer.admin.entity.domain.manage.Menu;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MenuPoJo {
  private Long id;
  private Integer state;
  private String name;
  private String url;
  private Boolean leaf;
  private String icon;
  private Long pId;
  private String pName;
  private int sort;
  private Date createTime;
  private Date updateTime;

  public MenuPoJo(
      final Long id,
      final Integer state,
      final String name,
      final String url,
      final Boolean leaf,
      final String icon,
      final Long pId,
      final int sort,
      final Date createTime,
      final Date updateTime) {
    this.id = id;
    this.state = state;
    this.name = name;
    this.url = url;
    this.leaf = leaf;
    this.icon = icon;
    this.pId = pId;
    this.sort = sort;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }

  public static MenuPoJo of(Menu menu) {
    if (menu == null) return null;
    MenuPoJo menuPoJo = new MenuPoJo();
    menuPoJo.setId(menu.getId());
    menuPoJo.setState(menu.getState());
    menuPoJo.setIcon(menu.getIcon());
    menuPoJo.setLeaf(menu.getLeaf());
    menuPoJo.setName(menu.getName());
    if (menu.getParent() != null) {
      menuPoJo.setPId(menu.getParent().getId());
      menuPoJo.setPName(menu.getParent().getName());
    }
    menuPoJo.setSort(menu.getSort());
    menuPoJo.setUrl(menu.getUrl());
    menuPoJo.setCreateTime(menu.getCreateTime());
    menuPoJo.setUpdateTime(menu.getUpdateTime());
    return menuPoJo;
  }
}
