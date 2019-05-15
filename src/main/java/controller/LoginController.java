package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import entity.User;
import org.thymeleaf.context.Context;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * �������е�¼ע��ģ������Ŀ�����
 *
 * @author Tedu
 */
public class LoginController {

    //��ӭҳ
    @RequestMapping("/")
    public String toMain() {
        return "index";
    }

    // ��¼������ת����¼ҳ
    @RequestMapping("/toLogin.do")
    public String toLogin(HttpServletRequest request, Context ctx) throws UnsupportedEncodingException {
        // ����벩�ͣ��ȼ���Ƿ��Ѿ���ͨ������û������Ƿ��Ѿ��й���Ա�û�
        boolean isreg = BlogsContext.getInstance().getUserService().chekcAdminRole();
        // ����Ѿ�ע��
        if (isreg) {
            // ���cookie����û���Զ���¼
            Cookie[] cs = request.getCookies();
            String name = null;
            String pwd = null;
            // ����cookie
            for (Cookie c : cs) {
                // ȡ���û���
                if (c.getName().equals("user_name")) {
                    name = URLDecoder.decode(c.getValue(), "utf-8");
                }
                // ȡ������
                if (c.getName().equals("user_pwd")) {
                    pwd = c.getValue();
                }
            }
            // ����û��������붼��ȡ��
            if (name != null && pwd != null) {
                // ʹ���û����������¼
                User u = BlogsContext.getInstance().getUserService().login(name, pwd);
                // �����¼�ɹ�
                if (u != null) {
                    // ���û�����session
                    HttpSession session = request.getSession();
                    session.setAttribute("user", u);
                    // ��ת����ҳ
                    return "redirect:main.do";
                }
            }
            ctx.setVariable("ca", request.getParameter("ca"));
            return "login";
        } else
            return "register";
    }

    // ��¼�ύ����֤��¼��Ϣ
    @RequestMapping("/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String un = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        User u = BlogsContext.getInstance().getUserService().login(un, pwd);
        // System.out.println("user:" + u);
        // ��¼�ɹ�
        if (u != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
            // �ж��Զ���¼
            String auto = request.getParameter("auto");
            if (auto != null) {
                Cookie cn = new Cookie("user_name", URLEncoder.encode(un, "utf-8"));
                Cookie cp = new Cookie("user_pwd", pwd);
                // ������Чʱ��
                cn.setMaxAge(60 * 30);
                cp.setMaxAge(60 * 30);
                // ���û������������cookie
                response.addCookie(cn);
                response.addCookie(cp);
            }
            return "redirect:main.do";
        } else {
            // ��¼ʧ��
            return "redirect:toLogin.do?ca=1";
        }
    }

    // ע��
    @RequestMapping("/register.do")
    public String register(HttpServletRequest request) {
        String username = request.getParameter("username");
        String useremail = request.getParameter("useremail");
        String pwd = request.getParameter("pwd");
        //String repwd = request.getParameter("repwd");
        User u = new User(0, useremail, username, "", pwd, "adminRole", 0, 0, "default.jpg");
        //ע���û�
        @SuppressWarnings("unused")
        int i = BlogsContext.getInstance().getUserService().registerUser(u);
        return "redirect:toLogin.do";
    }

    // ��ʾ��ҳ��
    @RequestMapping("/main.do")
    public String list(HttpServletRequest request, Context ctx) {
        // �����ҳ��Ϣ
        Map<String, Object> mainInfo = BlogsContext.getInstance().getUserService().getMainInfo();
        HttpSession session = request.getSession();
        // ����Ự
        session.setAttribute("info", mainInfo);
        // ת����main.jsp
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        //System.out.println("session:"+session.getAttribute("indexparam")+","+session.getAttribute("typeparam"));
        return "main";
    }
}
