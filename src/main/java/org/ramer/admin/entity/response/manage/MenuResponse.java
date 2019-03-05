package org.ramer.admin.entity.response.manage;

import java.util.List;
import lombok.*;
import org.ramer.admin.entity.pojo.manage.MenuPoJo;
import org.springframework.beans.BeanUtils;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MenuResponse {
  private Long id;
  private String name;
  private String url;
  private Boolean leaf;
  private String icon;
  private Long pId;
  private List<MenuResponse> children;

  public static MenuResponse of(MenuPoJo menuPoJo) {
    MenuResponse response = new MenuResponse();
    BeanUtils.copyProperties(menuPoJo, response);
    return response;
  }
}
