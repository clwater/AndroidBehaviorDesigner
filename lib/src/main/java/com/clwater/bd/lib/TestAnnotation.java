package com.clwater.bd.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface TestAnnotation {
    String value() default  "default";
    String SUFFIX = "_TestAnnotation";
}