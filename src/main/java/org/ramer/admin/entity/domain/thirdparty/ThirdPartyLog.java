package org.ramer.admin.entity.domain.thirdparty;

import org.ramer.admin.entity.AbstractEntity;
import javax.persistence.*;
import javax.persistence.Entity;

import lombok.*;

/**
 * 三方系统请求日志.
 *
 * @author ramer
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyLog extends AbstractEntity {
  @ManyToOne
  @JoinColumn(referencedColumnName = "code")
  private ThirdPartyCertificate thirdPartyCertificate;
  /** 请求地址 */
  private String url;
  /** 请求IP */
  private String ip;
  /** 请求结果 */
  @Column(columnDefinition = "text")
  private String result;
}
