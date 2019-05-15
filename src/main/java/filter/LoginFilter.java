package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 获得请求的路径
        String path = request.getRequestURI();
        // 如果路径是.do结尾，则是访问控制器的请求
        if (path.substring(path.lastIndexOf(".") + 1).equals("do")) {
            // 定义白名单
            String[] whiteList = {"toLogin.do", "login.do", "register.do"};
            for (String uri : whiteList) {
                // 如果请求的是白名单中的任意一个路径
                if (path.substring(path.lastIndexOf("/") + 1).equals(uri)) {
                    // 允许访问
                    chain.doFilter(request, response);
                    return;
                }
            }
            // 如果请求的路径不在白名单中，就需要登录后才能访问
            // 如果没有登录
            if (request.getSession().getAttribute("user") == null) {
                // 返回登录页面
                response.sendRedirect("toLogin.do");
                return;
            }
        }
        // 如果已经登录，或者访问的不是控制器（css,js,图片等）,允许访问
        chain.doFilter(request, response);
    }

    public void init(FilterConfig arg0) throws ServletException {

    }

}
