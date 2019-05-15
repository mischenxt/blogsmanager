package base.common;

import java.lang.reflect.Method;

//一条注解记录和一个类中某个方法的映射
public class Handler {
    //要调用的方法
    private Method method;
    //要调用方法的对象
    private Object object;

    public Handler() {
    }

    public Handler(Method method, Object object) {
        super();
        this.method = method;
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}