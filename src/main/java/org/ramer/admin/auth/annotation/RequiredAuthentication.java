package org.ramer.admin.auth.annotation;

import java.lang.annotation.*;

/**
 * 三方请求认证注解.
 *
 * @author ramer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredAuthentication {}
