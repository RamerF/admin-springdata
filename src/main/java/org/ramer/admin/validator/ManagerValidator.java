package org.ramer.admin.validator;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.domain.manage.Manager;
import org.ramer.admin.entity.pojo.manage.ManagerPoJo;
import org.ramer.admin.service.manage.system.ManagerService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Slf4j
@Component
public class ManagerValidator implements Validator {
  @Resource ManagerService service;

  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Manager.class) || clazz.isAssignableFrom(ManagerPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    Manager manager = (Manager) target;
    if (manager == null) {
      errors.rejectValue("manager", "manager.null", "参数不能为空");
    } else {
      if (!StringUtils.isEmpty(manager.getPassword())
          && !manager.getPassword().matches("^[a-zA-Z]\\w{5,17}$")) {
        errors.rejectValue(
            "password", "manager.password.length", "密码 必须以字母开头,长度在6~18之间,只能包含字符,数字和下划线");
      }
      if (StringUtils.isEmpty(manager.getName()) || manager.getName().length() > 25) {
        errors.rejectValue("name", "manager.name.length", "名称 不能为空且小于25个字符");
      }
      if (StringUtils.isEmpty(manager.getEmpNo()) || manager.getEmpNo().length() > 25) {
        errors.rejectValue("empNo", "manager.empNo.length", "员工号 不能为空且小于25个字符");
      } else {
        final Manager byEmpNo = service.getByEmpNo(manager.getEmpNo());
        if (byEmpNo != null
            && !byEmpNo.getId().equals(manager.getId())
            && byEmpNo.getState().equals(Constant.STATE_ON)) {
          errors.rejectValue("empNo", "manager.empNo.exist", "员工号 已存在");
        }
      }
      if (StringUtils.isEmpty(manager.getPhone())) {
        errors.rejectValue("phone", "manager.phone.empty", "手机号 不能为空");
      } else if (!manager.getPhone().matches("^1[3-8][0-9]{9}$")) {
        errors.rejectValue("phone", "manager.phone.length", "手机号 格式不正确");
      }
      if (!StringUtils.isEmpty(manager.getAvatar()) && manager.getAvatar().length() > 50) {
        errors.rejectValue("avatar", "manager.avatar.length", "图片 不能大于50个字符");
      }
      if (StringUtils.isEmpty(manager.getGender())
          || !Constant.Gender.ordinalList().contains(manager.getGender())) {
        errors.rejectValue("gender", "manager.gender.empty", " 性别 不能为空且只能为0或1");
      }
    }
  }
}
