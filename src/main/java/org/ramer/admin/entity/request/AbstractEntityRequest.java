package org.ramer.admin.entity.request;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ramer.admin.entity.AbstractEntity;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityRequest implements Serializable {
  long serialVersionUID = 1L;

  private Long id;

  /**
   * Request实体转换为Domain实体的额外处理.
   *
   * @param entity 业务请求实体 {@link AbstractEntityRequest}.
   * @param domain Domain实体 {@link AbstractEntity}.
   */
  @SuppressWarnings({"unused"})
  public <T extends AbstractEntity, E extends AbstractEntityRequest> void of(E entity, T domain) {}
}
