package org.ramer.admin.validator;

import org.ramer.admin.entity.domain.manage.Config;
import org.ramer.admin.entity.pojo.manage.ConfigPoJo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/** @author ramer */
@Component
public class ConfigValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Config.class) || clazz.isAssignableFrom(ConfigPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    Config config = (Config) target;
    if (config == null) {
      errors.rejectValue(null, "config.null", "参数不能为空");
    } else {
      if (StringUtils.isEmpty(config.getCode()) || config.getCode().length() > 50) {
        errors.rejectValue("code", "config.code.length", "code 不能为空且小于50个字符");
      }
      if (StringUtils.isEmpty(config.getName()) || config.getName().length() > 50) {
        errors.rejectValue("name", "config.name.length", "名称 不能为空且小于50个字符");
      }
      if (StringUtils.isEmpty(config.getValue())) {
        errors.rejectValue("value", "config.value.empty", "参数值 不能为空");
      }
      String remark = config.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 100) {
        errors.rejectValue("remark", "config.remark.length", "备注 不能超过100个字符");
      }
    }
  }
}
