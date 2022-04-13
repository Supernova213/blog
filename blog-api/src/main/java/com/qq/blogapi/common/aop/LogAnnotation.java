package com.qq.blogapi.common.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
//Type代表可以放在类上面，METHOD代表可以放在方法上
@Target({ElementType.METHOD})
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operater() default  "";
}
