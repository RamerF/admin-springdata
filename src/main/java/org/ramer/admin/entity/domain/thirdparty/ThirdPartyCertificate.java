package org.ramer.admin.entity.domain.thirdparty;

import org.ramer.admin.entity.AbstractEntity;
import javax.persistence.*;
import javax.persistence.Entity;

import lombok.*;

/** @author ramer */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyCertificate extends AbstractEntity {
  @Column(nullable = false, length = 50)
  private String code;

  @Column(nullable = false)
  private String secret;

  private String name;
  private String remark;
}
