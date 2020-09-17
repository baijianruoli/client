package io.github.baijianruoli.lidou.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD})
public @interface Reference {
    String value() default "LIQIQI";
}