package org.ramer.admin;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** @author Ramer @Date 02/13/2018 */
public class MainGenerator {
  private static String lineSeparator = System.getProperty("line.separator");

  public static void main(String[] args) throws Exception {
    System.out.println(
        "===================================================================================");
    System.out.println(
        "=======================    admin-springdata代码生成器 v1.0.0    ====================");
    System.out.println(
        "=======================    Author Ramer                        ====================");
    System.out.println(
        "===================================================================================");
    System.out.println();

    String classPath = null;
    String domainPath = null;
    String aliaName = null;
    String description = null;
    String isCopy = null;

    String savingPathRepository = null;
    String savingPathService = null;
    String savingPathServiceImpl = null;
    String savingPathController = null;
    String savingPathValidator = null;
    String savingPathPoJo = null;
    String savingPathRequest = null;
    String savingPathResponse = null;

    // 读取配置文件信息
    final String path =
        MainGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    Properties properties = new Properties();
    final File configFile =
        new File(path.substring(0, path.lastIndexOf("/")).concat("/config.ini"));
    if (configFile.exists()) {
      properties.load(new InputStreamReader(new FileInputStream(configFile), "GBK"));
      classPath = properties.getProperty("classPath");
      domainPath = properties.getProperty("domainPath");
      aliaName = properties.getProperty("aliaName");
      description = properties.getProperty("description");
      isCopy = properties.getProperty("isCopy");
      if (isCopy.equals("y")) {
        savingPathRepository = properties.getProperty("savingPathRepository");
        savingPathService = properties.getProperty("savingPathService");
        savingPathServiceImpl = properties.getProperty("savingPathServiceImpl");
        savingPathController = properties.getProperty("savingPathController");
        savingPathValidator = properties.getProperty("savingPathValidator");
        savingPathPoJo = properties.getProperty("savingPathPoJo");
        savingPathRequest = properties.getProperty("savingPathRequest");
        savingPathResponse = properties.getProperty("savingPathResponse");
      }
    } else {
      System.out.println(
          " 请输入domain对象的包路径: 例如: org.ramer.demo.entity.domain.manage.system.Manager");
      Scanner scanner = new Scanner(System.in);
      domainPath = scanner.next();
      System.out.println(" 请输入domain对象的class目录: 例如: D:\\workspace\\dnc\\dnc-dao\\target\\classes");
      classPath = scanner.next();
      System.out.println(" 请输入对象的别名: 例如: manager");
      aliaName = scanner.next();
      System.out.println(" 请输入对象的中文描述: 例如: 管理员");
      description = scanner.next();
      System.out.println(" 复制到指定路径 ?(y/n)");
      isCopy = scanner.next();

      while (!isCopy.equals("y") && !isCopy.equals("n")) {
        System.out.println(" 输入有误,请重新输入(y/n)");
      }
      if (isCopy.equalsIgnoreCase("y")) {
        System.out.println(" 请输入repository保存路径: 例如: D:\\workspace\\demo\\repository");
        savingPathRepository = scanner.next();
        System.out.println(" 请输入service保存路径: 例如: D:\\workspace\\demo\\service");
        savingPathService = scanner.next();
        savingPathServiceImpl =
            Objects.isNull(savingPathService)
                ? savingPathService
                : savingPathService.concat("\\impl");
        System.out.println(" 请输入controller保存路径: 例如: D:\\workspace\\demo\\controller");
        savingPathController = scanner.next();
        System.out.println(" 请输入validator保存路径: 例如: D:\\workspace\\demo\\validator");
        savingPathValidator = scanner.next();
        System.out.println(" 请输入pojo保存路径: 例如: D:\\workspace\\demo\\pojo");
        savingPathPoJo = scanner.next();
        System.out.println(" 请输入request保存路径: 例如: D:\\workspace\\demo\\request");
        savingPathRequest = scanner.next();
        System.out.println(" 请输入response保存路径: 例如: D:\\workspace\\demo\\response");
        savingPathResponse = scanner.next();
      }
    }

    /** domain类名 */
    final String domainName = domainPath.substring(domainPath.lastIndexOf(".") + 1);
    /** 基础包路径 */
    final String packagePath = domainPath.substring(0, domainPath.indexOf(".entity"));
    final int subDirFrom = domainPath.indexOf("domain.") + 6;
    final int subDirTo = domainPath.indexOf(domainName) - 1;
    final String subDir = subDirFrom == subDirTo ? "" : domainPath.substring(subDirFrom, subDirTo);

    generateRepository(
        packagePath, domainName, subDir, aliaName, description, savingPathRepository);
    generateService(packagePath, domainName, subDir, aliaName, description, savingPathService);
    generateServiceImpl(
        packagePath, domainName, subDir, aliaName, description, savingPathServiceImpl);
    generateController(
        packagePath, domainName, subDir, aliaName, description, savingPathController);
    generateValidator(packagePath, domainName, subDir, aliaName, description, savingPathValidator);
    generatePoJo(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        domainPath,
        classPath,
        savingPathPoJo);
    generateRequest(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        domainPath,
        classPath,
        savingPathRequest);
    generateResponse(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        domainPath,
        classPath,
        savingPathResponse);
    System.out.println(
        "=======================               执行完成 !!!             ====================");
  }

  private static void generateResponse(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String domainPath,
      final String classPath,
      String savingPath)
      throws Exception {
    generateWithField(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        domainPath,
        classPath,
        savingPath,
        "Response");
  }

  private static void generateRequest(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String domainPath,
      final String classPath,
      String savingPath)
      throws Exception {
    generateWithField(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        domainPath,
        classPath,
        savingPath,
        "Request");
  }

  private static void generatePoJo(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String domainPath,
      final String classPath,
      String savingPath)
      throws Exception {
    generateWithField(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        domainPath,
        classPath,
        savingPath,
        "PoJo");
  }

  private static void generateValidator(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String savingPath)
      throws Exception {
    generateWithNoField(
        packagePath, domainName, subDir, aliaName, description, savingPath, "Validator");
  }

  private static void generateController(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String savingPath)
      throws Exception {
    generateWithNoField(
        packagePath, domainName, subDir, aliaName, description, savingPath, "Controller");
  }

  private static void generateServiceImpl(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String savingPath)
      throws Exception {
    generateWithNoField(
        packagePath, domainName, subDir, aliaName, description, savingPath, "ServiceImpl");
  }

  private static void generateService(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String savingPath)
      throws Exception {
    generateWithNoField(
        packagePath, domainName, subDir, aliaName, description, savingPath, "Service");
  }

  private static void generateRepository(
      String packagePath,
      String domainName,
      String subDir,
      String aliaName,
      String description,
      String savingPath)
      throws Exception {
    generateWithNoField(
        packagePath, domainName, subDir, aliaName, description, savingPath, "Repository");
  }

  private static File copyTemplateFile(final String domainName, final String suffix)
      throws IOException {
    final InputStream inputStream =
        ClassLoader.class.getResourceAsStream(
            "/generator-template/".concat(suffix).concat(".java"));
    final Path path = Files.createTempFile(domainName.concat(suffix), ".java");
    File file = path.toFile();
    file.deleteOnExit();
    if (Objects.isNull(inputStream)) {
      System.out.println(" 生成 ".concat(suffix).concat(" 失败 : 模板文件不存在"));
      return null;
    }
    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    return file;
  }

  private static StringBuilder loadFile(final File file, final FileReader fileReader) {
    StringBuilder stringBuilder = new StringBuilder();
    String lineStr;
    try (FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader =
            new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader)) {
      while ((lineStr = reader.readLine()) != null) {
        stringBuilder.append(lineStr).append(lineSeparator);
      }
    } catch (Exception e) {
      System.err.println(" 载入文件失败: ".concat(e.getMessage()));
    }
    return stringBuilder;
  }

  private static void generateWithNoField(
      final String packagePath,
      final String domainName,
      final String subDir,
      final String aliaName,
      final String description,
      final String savingPath,
      final String suffix)
      throws IOException {
    File file = copyTemplateFile(domainName, suffix);
    if (file == null) return;
    final FileReader fileReader = new FileReader(file);
    StringBuilder stringBuilder = loadFile(file, fileReader);
    writeFile(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        savingPath,
        suffix,
        file,
        fileReader,
        stringBuilder,
        new StringBuilder());
  }

  private static void generateWithField(
      final String packagePath,
      final String domainName,
      final String subDir,
      final String aliaName,
      final String description,
      final String domainPath,
      final String classPath,
      final String savingPath,
      final String suffix)
      throws IOException, ClassNotFoundException {
    File file = copyTemplateFile(domainName, suffix);
    if (file == null) return;
    final FileReader fileReader = new FileReader(file);
    StringBuilder stringBuilder = loadFile(file, fileReader);
    final StringBuilder fieldBuilder = new StringBuilder();
    buildField(domainPath, classPath, fieldBuilder);
    writeFile(
        packagePath,
        domainName,
        subDir,
        aliaName,
        description,
        savingPath,
        suffix,
        file,
        fileReader,
        stringBuilder,
        fieldBuilder);
  }

  private static void buildField(
      final String domainPath, final String classPath, final StringBuilder fieldBuilder)
      throws ClassNotFoundException {
    Class clazz;
    try {
      clazz =
          new URLClassLoader(new URL[] {new File(classPath).toURI().toURL()}).loadClass(domainPath);
    } catch (MalformedURLException e) {
      System.err.println(
          "=======================    class 文件不存在                     ====================");
      return;
    }
    final Map<Boolean, List<Field>> collect =
        Stream.of(clazz.getDeclaredFields())
            .filter(field -> Modifier.isPrivate(field.getModifiers()))
            .collect(Collectors.groupingBy(field -> field.getType().getName().contains("java.")));
    // 引用对应的字段名
    List<String> referenceNames = new ArrayList<>();
    Optional.ofNullable(collect.get(true))
        .orElseGet(ArrayList::new)
        .forEach(
            field -> {
              if (field.getType().getName().contains("List")) {
                fieldBuilder
                    .append(lineSeparator)
                    .append("  private List<Long> ")
                    .append(field.getName())
                    .append("Ids;")
                    .append(lineSeparator);
              } else {
                final String simpleName = field.getType().getSimpleName();
                fieldBuilder
                    .append(lineSeparator)
                    .append("  private ")
                    .append(simpleName)
                    .append(" ")
                    .append(field.getName())
                    .append(";")
                    .append(lineSeparator);
                if (simpleName.equals("Long")) {
                  referenceNames.add(field.getName());
                }
              }
            });
    Optional.ofNullable(collect.get(false)).orElseGet(ArrayList::new).stream()
        .filter(field -> !referenceNames.contains(field.getName()))
        .forEach(
            field ->
                fieldBuilder
                    .append(lineSeparator)
                    .append("  private Long ")
                    .append(field.getName())
                    .append("Id")
                    .append(";")
                    .append(lineSeparator));
  }

  private static void writeFile(
      final String packagePath,
      final String domainName,
      final String subDir,
      final String aliaName,
      final String description,
      final String savingPath,
      final String suffix,
      final File file,
      final FileReader fileReader,
      final StringBuilder stringBuilder,
      final StringBuilder fieldBuilder)
      throws IOException {
    final String content =
        stringBuilder
            .toString()
            .replace("${basePath}", packagePath)
            .replace("${name}", domainName)
            .replace("${alia}", aliaName)
            .replace("${description}", description)
            .replace("${subDir}", subDir)
            .replace("${fieldList}", fieldBuilder.toString());
    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
    while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
      randomAccessFile.setLength(0);
      randomAccessFile.seek(0);
      randomAccessFile.write(content.getBytes(StandardCharsets.UTF_8));
    }
    if (!Objects.isNull(savingPath) && !"".equals(savingPath)) {
      if (!new File(savingPath).exists() && !new File(savingPath).mkdirs()) {
        System.out.println(" 生成 ".concat(suffix).concat(" 失败 : 目录创建失败"));
        return;
      }
      final String filePath =
          savingPath.concat("\\").concat(domainName).concat(suffix).concat(".java");
      Files.copy(file.toPath(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
      System.out.println(" 生成 ".concat(suffix).concat(" 成功 : ").concat(filePath));
    } else {
      final File savingFile = new File(domainName.concat(suffix).concat(".java"));
      Files.copy(file.toPath(), Paths.get(savingFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
      System.out.println(
          " 生成 ".concat(suffix).concat(" 成功 : ").concat(savingFile.getAbsolutePath()));
    }
  }
}
