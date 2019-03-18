package ${basePath}.entity.pojo${subDir};

import ${basePath}.entity.domain.AbstractEntity;
import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.entity.pojo.AbstractEntityPoJo;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * ${description}.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ${name}PoJo extends AbstractEntityPoJo {
${fieldList}
  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    ${name} obj = (${name}) entity;
    ${name}PoJo poJo = (${name}PoJo) super.of(entity, clazz);
    // TODO-WARN: 添加 Domain 转 PoJo 规则,对象转id
    // 例如: poJo.setXxxId(Optional.ofNullable(obj.getXxx()).map(AbstractEntity::getId).orElse(null));
    return (T) poJo;
  }

  public static ${name}PoJo of(${name} entity) {
    return new ${name}PoJo().of(entity, ${name}PoJo.class);
  }
}
