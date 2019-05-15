package base.common;

import base.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerMapping {
    //��������·����ӦҪ���õķ�����map
    //String����·��
    //handlerֵ��Ҫ���õķ���
    private Map<String, Handler> handlerMap = new HashMap<String, Handler>();

    //������������Controller������󼯺�
    public void process(List<Object> beans) {
        //������������Controller�����󼯺ϣ�����ÿ�������������ܰ���һ����������
        //ÿ��������������һ��ע��·���ͷ�����ӳ��
        for (Object bean : beans) {
            //��ÿ�������ķ���
            @SuppressWarnings("rawtypes")
            Class clazz = bean.getClass();
            //��÷��������з���
            Method[] method = clazz.getDeclaredMethods();
            //������Щ����
            for (Method m : method) {
                //����ע�ڷ����ϵ�RequestMappingע��ȡ��
                RequestMapping rm = m.getDeclaredAnnotation(RequestMapping.class);
                //���ȡ����ע����Ĵ��ڣ�����null��
                if (rm != null) {
                    //��ע���value���
                    String path = rm.value();
                    //��·���Ͷ�Ӧ�ķ�����һ����뼯��
                    handlerMap.put(path, new Handler(m, bean));
                }
            }
        }
        //�������
        System.out.println("handlermap:" + handlerMap);
    }

    //�ṩһ����������ʹ��·��ֱ�Ӵӱ�������handlerMap������ֱ�ӻ��ֵ
    public Handler getHandler(String path) {
        //����keyΪpath��handler����
        return handlerMap.get(path);
    }
}