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

//核心控制器，在tomcat服务启动时实例化初始化这个类
public class DispatcherServlet extends HttpServlet {
    // 序列化参数（和业务本身无关）
    private static final long serialVersionUID = 1L;
    //Thymeleaf的模板引擎
    private static TemplateEngine templateEngine;
    // 包含路径和要调用方法映射集合的对象
    private HandlerMapping handlerMapping;

    // 初始化方法
    @Override
    public void init() throws ServletException {
        // 使用Dom4j解析
        SAXReader reader = new SAXReader();
        // 获得web.xml文件中此servlet配置的初始化参数
        // 获得名为configLocation的值，本项目中为smartmvc.xml
        String fileName = getServletConfig().getInitParameter("configLocation");
        // 使用输入流获得根目录下的smartmvc.xml
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            // 读入smartmvc.xml
            Document doc = reader.read(is);
            // 获得xml文件的根节点
            Element root = doc.getRootElement();
            // 获得所有跟节点的子节点
            @SuppressWarnings("unchecked")
            List<Element> elements = root.elements();
            // 每一个子节点指定了一个控制器类
            // 准备一个集合存放所有子节点指定的控制器类对象
            List<Object> beans = new ArrayList<Object>();
            // 遍历所有子节点
            for (Element el : elements) {
                // 取出子节点中class属性的属性值
                String className = el.attributeValue("class");
                // 输出这个属性值（可选）
                System.out.println("className：" + className);
                // 获得的属性值是个控制器类名
                // 根据类名获得这个类的反射，再由反射生成控制器类对象
                Object bean = Class.forName(className).newInstance();
                // 将这个控制器类对象放入刚刚准备好的集合
                beans.add(bean);
            }
            // 实例化本类中属性handlerMapping
            handlerMapping = new HandlerMapping();
            // 使用handlerMapping对象中编写好的解析控制器集合的方法
            // 方法会将每个控制器类中注解路径和方法的反射生产映射记录并保存
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

        //初始化Thymeleaf的模板引擎
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
        // 获得请求的路径（uri，从项目名开始到xxx.do）
        String uri = request.getRequestURI();
        // 获得项目名
        String contextPath = request.getContextPath();
        // 测试输出
        System.out.println("uri:" + uri);
        System.out.println("contextPath:" + contextPath);
        // 从uri中去除项目名的部分，获得纯路径（/xxx.do）
        String path = uri.substring(contextPath.length());
        // 测试输出
        System.out.println("path:" + path);
        // 从映射类中通过路径获得要调用的方法信息
        Handler handler = handlerMapping.getHandler(path);
        // 测试输出
        System.out.println("handler:" + handler);
        // 获得要调用的方法反射
        Method m = handler.getMethod();
        // 获得要调用的方法对象
        Object o = handler.getObject();
        //Thymeleaf上下文
        Context ctx = new Context();
        // 声明要调用方法的返回值
        Object returnVal = null;
        try {
            // 从方法反射中获得方法的所有参数的反射数组
            @SuppressWarnings("rawtypes")
            Class[] types = m.getParameterTypes();
            // 如果这个方法有参数
            if (types.length > 0) {
                // 声明一个数组接收参数
                Object[] params = new Object[types.length];
                // 遍历所有参数反射
                for (int i = 0; i < types.length; i++) {
                    // 检查这个参数类型是不是HttpServletRequest
                    if (types[i] == HttpServletRequest.class) {
                        // 如果是，将request对象放入数组
                        params[i] = request;
                    }
                    // 检查这个参数类型是不是HttpServletResponse
                    if (types[i] == HttpServletResponse.class) {
                        // 如果是，将response对象放入数组
                        params[i] = response;
                    }
                    // 检查这个参数类型是不是Context
                    if (types[i] == Context.class) {
                        // 如果是，将response对象放入数组
                        params[i] = ctx;
                    }
                }
                // 调用方法并接收方法的返回值
                // 这里调用时invoke方法参数o为调用方法的对象，param是这个方法的参数数组
                returnVal = m.invoke(o, params);
            } else {
                // 如果这个方法没有参数
                // 也是调用方法并接收返回值，只是invoke方法没有第二个参数
                returnVal = m.invoke(o);
            }
            // 输出返回值
            System.out.println("return:" + returnVal);
            // 转换返回值为String（这里只支持String返回值类型）
            String viewName = returnVal.toString();
            // 如果返回值以redirect:开头
            if (viewName.startsWith("redirect:")) {
                // 拼接路径
                String redirectPath = contextPath + "/" + viewName.substring("redirect:".length());
                // 重定向到该路径
                response.sendRedirect(redirectPath);
            } else {
                //使用Thymeleaf返回网页
                String result = templateEngine.process(viewName, ctx);
                response.setContentType("text/html;charset=utf-8");
                //将字符串输出到页面上
                PrintWriter writer = response.getWriter();
                // 保存处理结果
                writer.write(result);
                writer.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}