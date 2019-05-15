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
        // ��������·��
        String path = request.getRequestURI();
        // ���·����.do��β�����Ƿ��ʿ�����������
        if (path.substring(path.lastIndexOf(".") + 1).equals("do")) {
            // ���������
            String[] whiteList = {"toLogin.do", "login.do", "register.do"};
            for (String uri : whiteList) {
                // ���������ǰ������е�����һ��·��
                if (path.substring(path.lastIndexOf("/") + 1).equals(uri)) {
                    // �������
                    chain.doFilter(request, response);
                    return;
                }
            }
            // ��������·�����ڰ������У�����Ҫ��¼����ܷ���
            // ���û�е�¼
            if (request.getSession().getAttribute("user") == null) {
                // ���ص�¼ҳ��
                response.sendRedirect("toLogin.do");
                return;
            }
        }
        // ����Ѿ���¼�����߷��ʵĲ��ǿ�������css,js,ͼƬ�ȣ�,�������
        chain.doFilter(request, response);
    }

    public void init(FilterConfig arg0) throws ServletException {

    }

}
