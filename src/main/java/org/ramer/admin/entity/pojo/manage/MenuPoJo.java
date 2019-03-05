package org.ramer.admin.entity.pojo.manage;

import java.util.Date;
import lombok.*;
import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class MenuPoJo extends AbstractEntityPoJo {
  private String name;
  private String url;
  private Boolean leaf;
  private String icon;
  private Long pId;
  private String pName;
  private int sort;

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
    setId(id);
    setState(state);
    setName(name);
    setUrl(url);
    setLeaf(leaf);
    setIcon(icon);
    setPId(pId);
    setSort(sort);
    setCreateTime(createTime);
    setUpdateTime(updateTime);
  }

  public static MenuPoJo of(Menu menu) {
    MenuPoJo poJo = new MenuPoJo();
    poJo.setIcon(menu.getIcon());
    poJo.setLeaf(menu.getLeaf());
    poJo.setName(menu.getName());
    if (menu.getParent() != null) {
      poJo.setPId(menu.getParent().getId());
      poJo.setPName(menu.getParent().getName());
    }
    poJo.setSort(menu.getSort());
    poJo.setUrl(menu.getUrl());

    AbstractEntityPoJo.of(poJo, menu);
    return poJo;
  }
}
