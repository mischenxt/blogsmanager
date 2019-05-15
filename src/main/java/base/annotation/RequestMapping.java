package base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//注解标明在运行时生效
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    //注解标明本注解属性值（"value"是特殊属性名，赋值时默认为value赋值）
    public String value();

}
