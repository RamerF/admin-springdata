package org.ramer.admin.entity.request;

import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ramer.admin.entity.AbstractEntity;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityRequest {
  private Long id;

  /**
   * 将一些引用id转换成对应的引用.
   *
   * @param entity 业务请求实体 {@link AbstractEntityRequest}.
   * @param domain Domain实体 {@link AbstractEntity}.
   */
  @SuppressWarnings({"unused"})
  public <T extends AbstractEntity, E extends AbstractEntityRequest> void of(E entity, T domain) {}
}
