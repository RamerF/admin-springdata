package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.AbstractEntity;
import javax.persistence.*;
import javax.persistence.Entity;

import lombok.*;

/** 系统数据字典类别. */
@Entity(name = "data_dict_type")
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class DataDictType extends AbstractEntity {
  @Column(nullable = false, unique = true, length = 25)
  private String code;

  @Column(nullable = false, length = 25)
  private String name;

  @Column(length = 100)
  private String remark;
}
