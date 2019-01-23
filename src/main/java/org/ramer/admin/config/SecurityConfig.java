package org.ramer.admin.config;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.Constant.PrivilegeEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/** Created by RAMER on 5/22/2017. */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Resource private UserDetailsService customUserService;
  @Resource private SecurityEncrypt securityEncrypt;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/style/**", "/script/**", "/font/**", "/base_layout", "/login")
        .permitAll()
        //                .antMatchers("/user/**")
        //                .hasAnyAuthority("USER", "ADMIN")
        .antMatchers("/manage/**")
        .hasAnyAuthority(
            PrivilegeEnum.GLOBAL.name + ":" + PrivilegeEnum.READ.name,
            PrivilegeEnum.MANAGE.name + ":" + PrivilegeEnum.READ.name)
        .antMatchers("/manage/signIn")
        .permitAll()
        .antMatchers("/manage/login")
        .permitAll()
        //
        //        .antMatchers(
        //            "/v2/api-docs",
        //            "/swagger-resources/configuration/ui",
        //            "/swagger-resources",
        //            "/swagger-resources/configuration/security",
        //            "/swagger-ui.html",
        //            "/webjars/**")
        //        .permitAll()
        //
        .and()
        .formLogin()
        .loginPage("/manage/login")
        .successForwardUrl("/manage/sign_in")
        .failureForwardUrl("/manage/sign_in")
        .and()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/manage/logout"))
        .logoutSuccessUrl("/manage/login")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .permitAll();
    http.exceptionHandling().accessDeniedPage("/forbidden");
    // TODO-TIP: 临时禁用
    http.csrf().disable();
    //    http.authorizeRequests()
    //        .antMatchers(
    //            "/v2/api-docs",
    //            "/swagger-resources/configuration/ui",
    //            "/swagger-resources",
    //            "/swagger-resources/configuration/security",
    //            "/swagger-ui.html",
    //            "/webjars/**")
    //        .permitAll();
    //        .and()
    //        .authorizeRequests()
    //        .anyRequest()
    //        .authenticated();
    //        .and()
    //        .csrf()
    //        .disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(securityEncrypt);
    auth.userDetailsService(customUserService);
  }
}
