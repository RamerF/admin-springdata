package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.AbstractEntity;
import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.Where;

import org.ramer.admin.entity.Constant;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

/** 系统数据字典. */
@Entity(name = "data_dict")
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DataDict extends AbstractEntity {
  @Where(clause = "state = " + Constant.STATE_ON)
  @ManyToOne
  @JoinColumn(name = "data_dict_type_id")
  @JsonBackReference
  private DataDictType dataDictType;

  @Column(nullable = false, length = 25)
  private String name;

  @Column(nullable = false, length = 25)
  private String code;

  @Column(length = 100)
  private String remark;

  public DataDict(Long id) {
    setId(id);
  }
}
