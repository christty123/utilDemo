package com.util.http;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Nick_Xiong
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface FieldRules {
       
   
    
    boolean  nullable() default true;
    
    boolean isNumeric() default false;
    
    String format() default "";//格式化字符串，如日期
    
}
