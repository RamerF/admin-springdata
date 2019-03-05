package org.ramer.admin.entity.request;

import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ramer.admin.entity.AbstractEntity;
import org.springframework.beans.BeanUtils;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityRequest {
  public static <T extends AbstractEntity, R extends AbstractEntityRequest> void of(R r, T t) {
    BeanUtils.copyProperties(r, t);
  }
}
