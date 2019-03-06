package org.ramer.admin.entity.pojo.manage;

import lombok.*;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/**
 * 三方调用凭证.
 *
 * @author ramer @Date 3/6/2019
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThirdPartyCertificatePoJo extends AbstractEntityPoJo {
  private String code;
  private String secret;
  private String name;
  private String remark;
}
