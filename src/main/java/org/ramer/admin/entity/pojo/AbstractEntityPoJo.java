package org.ramer.admin.entity.pojo;

import java.util.Date;
import java.util.Objects;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ramer.admin.entity.AbstractEntity;
import org.springframework.beans.BeanUtils;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityPoJo {
  private Long id;
  private Integer state;
  private Date createTime;
  private Date updateTime;

  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    T poJo;
    try {
      poJo = clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    BeanUtils.copyProperties(entity, poJo);
    return poJo;
  }
}
