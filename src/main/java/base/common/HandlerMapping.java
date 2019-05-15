package base.common;

import base.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerMapping {
    //包含所有路径对应要调用的方法的map
    //String键是路径
    //handler值是要调用的方法
    private Map<String, Handler> handlerMap = new HashMap<String, Handler>();

    //解析控制器（Controller）类对象集合
    public void process(List<Object> beans) {
        //遍历控制器（Controller）对象集合，其中每个控制器都可能包含一个或多个方法
        //每个方法都可能是一对注解路径和方法的映射
        for (Object bean : beans) {
            //获得控制器类的反射
            @SuppressWarnings("rawtypes")
            Class clazz = bean.getClass();
            //获得反射中所有方法
            Method[] method = clazz.getDeclaredMethods();
            //遍历这些方法
            for (Method m : method) {
                //将标注在方法上的RequestMapping注解取出
                RequestMapping rm = m.getDeclaredAnnotation(RequestMapping.class);
                //如果取出的注解真的存在（不是null）
                if (rm != null) {
                    //将注解的value获得
                    String path = rm.value();
                    //将路径和对应的方法做一组放入集合
                    handlerMap.put(path, new Handler(m, bean));
                }
            }
        }
        //输出测试
        System.out.println("handlermap:" + handlerMap);
    }

    //提供一个方法方便使用路径直接从本类对象的handlerMap属性中直接获得值
    public Handler getHandler(String path) {
        //返回key为path的handler对象
        return handlerMap.get(path);
    }
}