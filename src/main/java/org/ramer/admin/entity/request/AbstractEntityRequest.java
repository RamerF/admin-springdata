package org.ramer.admin.entity.request;

import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntityRequest {
  private Long id;
}
