package org.ramer.admin.validator;

import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.entity.pojo.manage.DataDictPoJo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class DataDictValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(DataDict.class) || clazz.isAssignableFrom(DataDictPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    DataDict dataDict = (DataDict) target;
    if (dataDict == null) {
      errors.rejectValue("dataDict", "dataDict.null", "参数不能为空");
    } else {
      if (StringUtils.isEmpty(dataDict.getName()) || dataDict.getName().length() > 25) {
        errors.rejectValue("name", "dataDict.name.length", "名称 不能为空且小于25个字符");
      }
      if (StringUtils.isEmpty(dataDict.getCode()) || dataDict.getCode().length() > 25) {
        errors.rejectValue("code", "dataDict.code.length", "code 不能为空且小于25个字符");
      }
      String remark = dataDict.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 100) {
        errors.rejectValue("remark", "dataDict.remark.length", "备注 不能超过100个字符");
      }
    }
  }
}
