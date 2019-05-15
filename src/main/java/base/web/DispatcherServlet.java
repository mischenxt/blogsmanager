package base.web;

import base.common.Handler;
import base.common.HandlerMapping;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//���Ŀ���������tomcat��������ʱʵ������ʼ�������
public class DispatcherServlet extends HttpServlet {
    // ���л���������ҵ�����޹أ�
    private static final long serialVersionUID = 1L;
    //Thymeleaf��ģ������
    private static TemplateEngine templateEngine;
    // ����·����Ҫ���÷���ӳ�伯�ϵĶ���
    private HandlerMapping handlerMapping;

    // ��ʼ������
    @Override
    public void init() throws ServletException {
        // ʹ��Dom4j����
        SAXReader reader = new SAXReader();
        // ���web.xml�ļ��д�servlet���õĳ�ʼ������
        // �����ΪconfigLocation��ֵ������Ŀ��Ϊsmartmvc.xml
        String fileName = getServletConfig().getInitParameter("configLocation");
        // ʹ����������ø�Ŀ¼�µ�smartmvc.xml
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            // ����smartmvc.xml
            Document doc = reader.read(is);
            // ���xml�ļ��ĸ��ڵ�
            Element root = doc.getRootElement();
            // ������и��ڵ���ӽڵ�
            @SuppressWarnings("unchecked")
            List<Element> elements = root.elements();
            // ÿһ���ӽڵ�ָ����һ����������
            // ׼��һ�����ϴ�������ӽڵ�ָ���Ŀ����������
            List<Object> beans = new ArrayList<Object>();
            // ���������ӽڵ�
            for (Element el : elements) {
                // ȡ���ӽڵ���class���Ե�����ֵ
                String className = el.attributeValue("class");
                // ����������ֵ����ѡ��
                System.out.println("className��" + className);
                // ��õ�����ֵ�Ǹ�����������
                // ����������������ķ��䣬���ɷ������ɿ����������
                Object bean = Class.forName(className).newInstance();
                // �������������������ո�׼���õļ���
                beans.add(bean);
            }
            // ʵ��������������handlerMapping
            handlerMapping = new HandlerMapping();
            // ʹ��handlerMapping�����б�д�õĽ������������ϵķ���
            // �����Ὣÿ������������ע��·���ͷ����ķ�������ӳ���¼������
            handlerMapping.process(beans);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //��ʼ��Thymeleaf��ģ������
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("../");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("utf-8");
        templateEngine.setTemplateResolver(resolver);
        templateEngine.isInitialized();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ��������·����uri������Ŀ����ʼ��xxx.do��
        String uri = request.getRequestURI();
        // �����Ŀ��
        String contextPath = request.getContextPath();
        // �������
        System.out.println("uri:" + uri);
        System.out.println("contextPath:" + contextPath);
        // ��uri��ȥ����Ŀ���Ĳ��֣���ô�·����/xxx.do��
        String path = uri.substring(contextPath.length());
        // �������
        System.out.println("path:" + path);
        // ��ӳ������ͨ��·�����Ҫ���õķ�����Ϣ
        Handler handler = handlerMapping.getHandler(path);
        // �������
        System.out.println("handler:" + handler);
        // ���Ҫ���õķ�������
        Method m = handler.getMethod();
        // ���Ҫ���õķ�������
        Object o = handler.getObject();
        //Thymeleaf������
        Context ctx = new Context();
        // ����Ҫ���÷����ķ���ֵ
        Object returnVal = null;
        try {
            // �ӷ��������л�÷��������в����ķ�������
            @SuppressWarnings("rawtypes")
            Class[] types = m.getParameterTypes();
            // �����������в���
            if (types.length > 0) {
                // ����һ��������ղ���
                Object[] params = new Object[types.length];
                // �������в�������
                for (int i = 0; i < types.length; i++) {
                    // ���������������ǲ���HttpServletRequest
                    if (types[i] == HttpServletRequest.class) {
                        // ����ǣ���request�����������
                        params[i] = request;
                    }
                    // ���������������ǲ���HttpServletResponse
                    if (types[i] == HttpServletResponse.class) {
                        // ����ǣ���response�����������
                        params[i] = response;
                    }
                    // ���������������ǲ���Context
                    if (types[i] == Context.class) {
                        // ����ǣ���response�����������
                        params[i] = ctx;
                    }
                }
                // ���÷��������շ����ķ���ֵ
                // �������ʱinvoke��������oΪ���÷����Ķ���param����������Ĳ�������
                returnVal = m.invoke(o, params);
            } else {
                // ����������û�в���
                // Ҳ�ǵ��÷��������շ���ֵ��ֻ��invoke����û�еڶ�������
                returnVal = m.invoke(o);
            }
            // �������ֵ
            System.out.println("return:" + returnVal);
            // ת������ֵΪString������ֻ֧��String����ֵ���ͣ�
            String viewName = returnVal.toString();
            // �������ֵ��redirect:��ͷ
            if (viewName.startsWith("redirect:")) {
                // ƴ��·��
                String redirectPath = contextPath + "/" + viewName.substring("redirect:".length());
                // �ض��򵽸�·��
                response.sendRedirect(redirectPath);
            } else {
                //ʹ��Thymeleaf������ҳ
                String result = templateEngine.process(viewName, ctx);
                response.setContentType("text/html;charset=utf-8");
                //���ַ��������ҳ����
                PrintWriter writer = response.getWriter();
                // ���洦����
                writer.write(result);
                writer.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}