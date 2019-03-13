package ${basePath}.entity.request${subDir};

import ${basePath}.entity.request.AbstractEntityRequest;
import ${basePath}.entity.domain.AbstractEntity;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import lombok.*;

/**
 * ${description}.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ${name}Request extends AbstractEntityRequest {
${fieldList}
  @Override
  public <T extends AbstractEntity, E extends AbstractEntityRequest> void of(E entity, T domain) {
      if (!Objects.isNull(domain)) {
        ${name}Request request = (${name}Request) entity;
        ${name} ${alia} = (${name}) domain;
        // TODO-WARN: 将 Request 对象转换成 Domain 对象,如果没有引用,该方法可以删除.
      }
  }
}
