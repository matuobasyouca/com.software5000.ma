package com.software5000.base;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.FIELD, ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented//说明该注解将被包含在javadoc中

/**
 * 实体中的字段加上该注解就不会被<code>BaseDao</code>基类所解析
 */
public @interface NotDatabaseField {
}
