package org.ramer.admin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@Slf4j
@Setter
// @Configuration
// @ConfigurationProperties(prefix = "spring.datasource")
public class DruidConfiguration {
  private String name;
  private String url;
  private String username;
  private String password;
  private String type;
  private String driverClassName;
  private boolean sessionStatEnable;
  private int maxActive;
  private int initialSize;
  private int maxWait;
  private int minIdle;
  private int timeBetweenEvictionRunsMillis;
  private int minEvictableIdleTimeMillis;
  private boolean testWhileIdle;
  private boolean testOnBorrow;
  private boolean testOnReturn;
  private String validationQuery;
  private int maxOpenPreparedStatements;

  @Bean
  public ServletRegistrationBean druidServlet() {
    log.info("init Druid Servlet Configuration ");
    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
    servletRegistrationBean.setServlet(new StatViewServlet());
    servletRegistrationBean.addUrlMappings("/druid/*");
    Map<String, String> initParameters = new HashMap<>();
    initParameters.put("loginUsername", "admin"); // 用户名
    initParameters.put("loginPassword", "admin"); // 密码
    initParameters.put("resetEnable", "false"); // 禁用HTML页面上的“Reset All”功能
    initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
    // initParameters.put("deny", "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
    servletRegistrationBean.setInitParameters(initParameters);
    return servletRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new WebStatFilter());
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.addInitParameter(
        "exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    try {
      //            dataSource.setFilters("stat, wall");
      getDataSource().setFilters("stat, wall");
    } catch (SQLException e) {
      log.error(e.getMessage(), e);
    }
    return filterRegistrationBean;
  }

  @Bean
  public DruidDataSource getDataSource() {
    DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
    dataSource.setName(this.name);
    dataSource.setUrl(this.url);
    dataSource.setUsername(this.username);
    dataSource.setPassword(this.password);
    dataSource.setDbType(this.type);
    dataSource.setDriverClassName(this.driverClassName);
    dataSource.setUseLocalSessionState(this.sessionStatEnable);
    dataSource.setMaxActive(this.maxActive);
    dataSource.setInitialSize(this.initialSize);
    dataSource.setMaxWait(this.maxWait);
    dataSource.setMinIdle(this.minIdle);
    dataSource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
    dataSource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
    dataSource.setTestOnBorrow(this.testOnBorrow);
    dataSource.setTestOnReturn(this.testOnReturn);
    dataSource.setTestWhileIdle(this.testWhileIdle);
    dataSource.setValidationQuery(this.validationQuery);
    dataSource.setMaxOpenPreparedStatements(this.maxOpenPreparedStatements);

    return dataSource;
  }
}
