package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.AbstractEntity;
import javax.persistence.*;
import lombok.*;

/** 系统参数. */
@Entity(name = "config")
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Config extends AbstractEntity {
  @Column(nullable = false, length = 50)
  private String code;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, columnDefinition = "text")
  private String value;

  @Column(length = 100)
  private String remark;
}
