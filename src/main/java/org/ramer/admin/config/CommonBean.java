package org.ramer.admin.config;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.filter.HttpPutFormContentFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** @author ramer */
@Configuration
@EnableSwagger2
public class CommonBean {
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool())
        .build();
  }

  @Bean
  public MappingJackson2HttpMessageConverter messageConverter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(new ObjectMapper());
    return converter;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("org.ramer.admin.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("项目接口文档")
        .description("api 根目录：http://192.168.0.98:8081/")
        .contact(new Contact("Tang Xiaofeng", "", ""))
        .version("1.0")
        .build();
  }

  @Bean
  public HttpPutFormContentFilter httpPutFormContentFilter() {
    return new HttpPutFormContentFilter();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    String idForEncode = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(idForEncode, new BCryptPasswordEncoder());
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());
    return new DelegatingPasswordEncoder(idForEncode, encoders);
  }

  @Bean
  public WebServerFactoryCustomizer webServerFactoryCustomizer() {
    return (WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>)
        factory -> {
          ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/400");
          ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
          ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
          ErrorPage error500Page =
              new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/templateEngineException");
          factory.addErrorPages(error400Page, error401Page, error404Page, error500Page);
        };
  }
}
