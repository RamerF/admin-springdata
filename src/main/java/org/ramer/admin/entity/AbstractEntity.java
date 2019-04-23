package org.ramer.admin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/** 持久类的公有父类，定义了公有的属性，简化了持久类的书写 */
@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {
  long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "tinyint default 1")
  private Integer state = Constant.STATE_ON;

  @CreationTimestamp
  @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
  private Date createTime = new Date(); // 创建时间

  @UpdateTimestamp
  @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private Date updateTime = new Date(); // 修改时间
}
