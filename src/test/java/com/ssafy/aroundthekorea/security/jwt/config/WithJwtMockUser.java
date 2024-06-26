package com.ssafy.aroundthekorea.security.jwt.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = MockUserCustomFactory.class)
public @interface WithJwtMockUser {

	long id() default 1L;

	String[] role() default {"ROLE_USER"};

	String username() default "whyWhale";
}
