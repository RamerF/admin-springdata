package ${basePath}.entity.pojo${subDir};

import ${basePath}.entity.domain.AbstractEntity;
import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.entity.pojo.AbstractEntityPoJo;
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
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    ${name} ${alia} = (${name}) entity;
    ${name}PoJo poJo = null;
    try {
      poJo = (${name}PoJo) clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    BeanUtils.copyProperties(${alia}, poJo);
    // TODO-WARN: 添加 Domain 转 PoJo 规则
    return (T) poJo;
  }
}
