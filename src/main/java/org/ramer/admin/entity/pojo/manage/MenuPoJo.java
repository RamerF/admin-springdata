package org.ramer.admin.entity.pojo.manage;

import java.util.Date;
import java.util.Objects;
import lombok.*;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.springframework.beans.BeanUtils;

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

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    Menu menu = (Menu) entity;
    MenuPoJo poJo = new MenuPoJo();
    if (menu.getParent() != null) {
      poJo.setPId(menu.getParent().getId());
      poJo.setPName(menu.getParent().getName());
    }
    BeanUtils.copyProperties(menu, poJo);
    return (T) poJo;
  }
}
