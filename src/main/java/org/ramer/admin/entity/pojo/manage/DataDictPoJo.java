package org.ramer.admin.entity.pojo.manage;

import java.util.Objects;
import lombok.*;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.springframework.beans.BeanUtils;

/** 系统数据字典. */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictPoJo extends AbstractEntityPoJo {
  private Long dataDictTypeId;
  private String name;
  private String code;
  private String remark;

  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends AbstractEntity, T extends AbstractEntityPoJo> T of(E entity, Class<T> clazz) {
    if (Objects.isNull(entity)) {
      return null;
    }
    DataDict dataDict = (DataDict) entity;
    DataDictPoJo poJo = new DataDictPoJo();
    if (dataDict.getDataDictType() != null) {
      poJo.setDataDictTypeId(dataDict.getDataDictType().getId());
    }
    BeanUtils.copyProperties(dataDict, poJo);
    return (T) poJo;
  }
}
