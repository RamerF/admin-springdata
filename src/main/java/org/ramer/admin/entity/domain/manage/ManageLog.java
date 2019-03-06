package org.ramer.admin.entity.domain.manage;

import org.ramer.admin.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * 管理端操作日志.
 *
 * @author ramer
 */
@Entity(name = "manage_log")
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class ManageLog extends AbstractEntity {
  @Column(length = 200)
  private String url;

  @OneToOne private Manager manager;

  @Column(length = 20)
  private String ip;

  @Column(columnDefinition = "text ")
  private String result;
}
