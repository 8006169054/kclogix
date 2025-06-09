package com.kclogix.common.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

	public String value();
	public int mergeOrder() default -1;
	public boolean merge() default false;
	public boolean date() default false;
	public boolean whitespace() default false;
	public String stringFormat() default "";
	public String prefix() default "";
	public String function() default "";
	
	
}
