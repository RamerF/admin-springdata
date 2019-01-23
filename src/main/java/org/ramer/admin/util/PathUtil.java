package org.ramer.admin.util;

import org.ramer.admin.entity.Constant.ResourcePath;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PathUtil {
  private static String resourceBasePath;

  private static String FILE_SEPARATOR = System.getProperty("file.separator");

  @Value("${org.ramer.resource.prefix}")
  public void setWinBasePath(String resourceBasePath) {
    PathUtil.resourceBasePath = resourceBasePath;
  }

  public static String getResourceBasePath() {
    return PathUtil.resourceBasePath;
  }

  public static String getImagePath(SavingFolder savingFolder) {
    String path;
    switch (savingFolder) {
      case MANAGER:
        path = SavingFolder.MANAGER.desc;
        break;
      case PUBLIC:
        path = SavingFolder.PUBLIC.desc;
        break;
      default:
        path = "/";
        break;
    }
    Date date = new Date();
    return path.concat(new SimpleDateFormat("yyyy").format(date))
        .concat(FILE_SEPARATOR)
        .concat(new SimpleDateFormat("MM").format(date))
        .concat(FILE_SEPARATOR)
        .concat(new SimpleDateFormat("dd").format(date))
        .concat(FILE_SEPARATOR)
        .replace("/", FILE_SEPARATOR);
  }

  public static String getResourcePath(String relativePath, SavingFolder savingFolder) {
    String path;
    switch (savingFolder) {
      case MANAGER:
        path = ResourcePath.MANAGER.concat(relativePath);
        break;
      case PUBLIC:
        path = ResourcePath.PUBLIC.concat(relativePath);
        break;
      default:
        path = ResourcePath.DEFAULT.concat(relativePath);
        break;
    }
    return path.replace("//", "/")
        .replace("\\\\", "\\")
        .replace("/", FILE_SEPARATOR)
        .replace("\\", FILE_SEPARATOR);
  }

  /** 文件保存目录. */
  public enum SavingFolder {
    MANAGER("/upload" + ResourcePath.MANAGER + "/"),
    PUBLIC("/upload" + ResourcePath.PUBLIC + "/");
    private String desc;

    SavingFolder(String desc) {
      this.desc = desc;
    }

    @Override
    public String toString() {
      return this.desc;
    }
  }
}
