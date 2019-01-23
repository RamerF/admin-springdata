package org.ramer.admin.validator;

import org.ramer.admin.entity.domain.manage.Menu;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/** @author ramer */
@Component
public class MenuValidator implements Validator {
  @Override
  public boolean supports(final Class<?> clazz) {
    return clazz.isAssignableFrom(Menu.class) || clazz.isAssignableFrom(MenuPoJo.class);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    Menu menu = (Menu) target;
    if (menu == null) {
      errors.rejectValue(null, "menu.null", "参数不能为空");
    } else {
      if (StringUtils.isEmpty(menu.getName())) {
        errors.rejectValue("name", "menu.name.empty", "名称 不能为空");
      }
      if (StringUtils.isEmpty(menu.getUrl()) || menu.getUrl().length() > 50) {
        errors.rejectValue("url", "menu.url.empty", "地址 不能为空且小于50个字符");
      }
      if (!StringUtils.isEmpty(menu.getIcon()) && menu.getIcon().length() > 25) {
        errors.rejectValue("icon", "menu.icon.length", "图标 应小于25个字符");
      }
      if (StringUtils.isEmpty(menu.getLeaf())) {
        errors.rejectValue("leaf", "menu.leaf.empty", "是否叶子节点 不能为空");
      }
      String remark = menu.getRemark();
      if (!StringUtils.isEmpty(remark) && remark.length() > 100) {
        errors.rejectValue("remark", "menu.remark.length", "备注 不能超过100个字符");
      }
    }
  }
}
