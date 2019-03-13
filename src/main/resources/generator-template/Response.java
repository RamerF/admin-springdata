package ${basePath}.entity.response${subDir};

import ${basePath}.entity.domain.AbstractEntity;
import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.entity.pojo${subDir}.ProjectPoJo;
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
public class ${name}Response {
${fieldList}
  public static ${name}Response of(${name} ${alia}) {
    ${name}Response poJo = new ${name}Response();
    // TODO-WARN:  将 Domain 对象转换成 Response 对象
    BeanUtils.copyProperties(${alia}, poJo);
    return poJo;
  }
}
