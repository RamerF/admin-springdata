package org.ramer.admin.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.ramer.admin.util.PathUtil.SavingFolder;
import org.springframework.util.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class ImageUtil {
  private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
  private static final Random r = new Random();

  /*
   * 生产随机文件名,当前年月日小时分钟秒钟
   */
  public static String getRandomFileName() {
    // 获取随机五位数
    int random = r.nextInt(89999) + 10000;
    String nowTimeStr = sDateFormat.format(new Date());
    return nowTimeStr + random;
  }

  /*
   * 保存图片缩略图.
   */
  public static String generateThumbnail(
      InputStream inputStream, String fileName, String targetAddr) throws Exception {
    makeDirPath(targetAddr);
    String relativeAddr = targetAddr + getRandomFileName() + getFileExtension(fileName);
    File dest = new File(PathUtil.getResourceBasePath() + relativeAddr);
    try {
      Thumbnails.of(inputStream).size(200, 200).outputQuality(0.8f).toFile(dest);
    } catch (Exception e) {
      log.warn(" ImageUtil.generateThumbnail : [{}]", e.getMessage());
      throw new Exception("获取图片url失败！");
    }

    return relativeAddr;
  }

  /*
   * 获取文件扩展名
   */
  private static String getFileExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }

  /*
   * 创建目标路径所涉及的目录
   */
  private static void makeDirPath(String relativePath) {
    String realFileParentPath = PathUtil.getResourceBasePath() + relativePath;
    File dirPath = new File(realFileParentPath);
    if (!dirPath.exists()) {
      dirPath.mkdirs();
    }
  }

  public static String save(
      MultipartFile file, String name, SavingFolder savingFolder, String fileSuffix) {
    String fileName = file.getOriginalFilename();
    Assert.notNull(fileName, "file name can not null");
    String rootDir = PathUtil.getImagePath(savingFolder);
    makeDirPath(rootDir);
    fileSuffix =
        StringUtils.isEmpty(fileSuffix)
            ? fileName.substring(fileName.lastIndexOf("."))
            : fileSuffix;
    String relativePath =
        rootDir.concat(StringUtils.isEmpty(name) ? getRandomFileName() : name).concat(fileSuffix);
    String absolutePath = PathUtil.getResourceBasePath().concat(relativePath);
    log.debug(" ImageUtil.save : saved file: [{},{}]", absolutePath, relativePath);
    try {
      //      file.transferTo(new File(absolutePath));
      FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(new File(absolutePath)));
      return PathUtil.getResourcePath(relativePath, savingFolder);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }
}
