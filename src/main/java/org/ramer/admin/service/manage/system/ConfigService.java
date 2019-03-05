package org.ramer.admin.service.manage.system;

import org.ramer.admin.entity.domain.manage.Config;
import org.ramer.admin.entity.pojo.manage.ConfigPoJo;
import org.ramer.admin.service.common.BaseService;

/** @author ramer */
public interface ConfigService extends BaseService<Config, ConfigPoJo> {

  /**
   * 获取网站描述信息.
   * <pre>
   * @param location:
   *      option value:<br/>
   *      {@code Constant.SITE_TITLE}<br/>
   *      {@code Constant.SITE_NAME}<br/>
   *      {@code Constant.SITE_COPYRIGHT}
   * </pre>
   */
  String getSiteInfo(String location);

  Config getByCode(String code);
}
