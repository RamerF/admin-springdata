package ${basePath}.validator${subDir};

import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.entity.pojo${subDir}.${name}PoJo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ${name}Validator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(${name}.class) || clazz.isAssignableFrom(${name}Request.class) || clazz.isAssignableFrom(${name}PoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    // TODO-WARN: 添加${description}校验规则
  }
}
