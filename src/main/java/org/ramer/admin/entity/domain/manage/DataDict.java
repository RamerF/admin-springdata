package org.ramer.admin.entity.domain.manage;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.*;
import org.ramer.admin.entity.AbstractEntity;

/**
 * 数据字典.
 *
 * @author ramer
 */
@Entity(name = "data_dict")
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DataDict extends AbstractEntity {
  @ManyToOne @JoinColumn @JsonBackReference private DataDictType dataDictType;

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
