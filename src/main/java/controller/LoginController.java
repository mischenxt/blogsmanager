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
 * 处理所有登录注册模块请求的控制器
 *
 * @author Tedu
 */
public class LoginController {

    //欢迎页
    @RequestMapping("/")
    public String toMain() {
        return "index";
    }

    // 登录链接跳转到登录页
    @RequestMapping("/toLogin.do")
    public String toLogin(HttpServletRequest request, Context ctx) throws UnsupportedEncodingException {
        // 想进入博客，先检查是否已经开通，检查用户表中是否已经有管理员用户
        boolean isreg = BlogsContext.getInstance().getUserService().chekcAdminRole();
        // 如果已经注册
        if (isreg) {
            // 检查cookie中有没有自动登录
            Cookie[] cs = request.getCookies();
            String name = null;
            String pwd = null;
            // 遍历cookie
            for (Cookie c : cs) {
                // 取出用户名
                if (c.getName().equals("user_name")) {
                    name = URLDecoder.decode(c.getValue(), "utf-8");
                }
                // 取出密码
                if (c.getName().equals("user_pwd")) {
                    pwd = c.getValue();
                }
            }
            // 如果用户名和密码都能取出
            if (name != null && pwd != null) {
                // 使用用户名和密码登录
                User u = BlogsContext.getInstance().getUserService().login(name, pwd);
                // 如果登录成功
                if (u != null) {
                    // 将用户放入session
                    HttpSession session = request.getSession();
                    session.setAttribute("user", u);
                    // 跳转到主页
                    return "redirect:main.do";
                }
            }
            ctx.setVariable("ca", request.getParameter("ca"));
            return "login";
        } else
            return "register";
    }

    // 登录提交，验证登录信息
    @RequestMapping("/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String un = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        User u = BlogsContext.getInstance().getUserService().login(un, pwd);
        // System.out.println("user:" + u);
        // 登录成功
        if (u != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
            // 判断自动登录
            String auto = request.getParameter("auto");
            if (auto != null) {
                Cookie cn = new Cookie("user_name", URLEncoder.encode(un, "utf-8"));
                Cookie cp = new Cookie("user_pwd", pwd);
                // 设置有效时间
                cn.setMaxAge(60 * 30);
                cp.setMaxAge(60 * 30);
                // 将用户名和密码存入cookie
                response.addCookie(cn);
                response.addCookie(cp);
            }
            return "redirect:main.do";
        } else {
            // 登录失败
            return "redirect:toLogin.do?ca=1";
        }
    }

    // 注册
    @RequestMapping("/register.do")
    public String register(HttpServletRequest request) {
        String username = request.getParameter("username");
        String useremail = request.getParameter("useremail");
        String pwd = request.getParameter("pwd");
        //String repwd = request.getParameter("repwd");
        User u = new User(0, useremail, username, "", pwd, "adminRole", 0, 0, "default.jpg");
        //注册用户
        @SuppressWarnings("unused")
        int i = BlogsContext.getInstance().getUserService().registerUser(u);
        return "redirect:toLogin.do";
    }

    // 显示主页的
    @RequestMapping("/main.do")
    public String list(HttpServletRequest request, Context ctx) {
        // 获得主页信息
        Map<String, Object> mainInfo = BlogsContext.getInstance().getUserService().getMainInfo();
        HttpSession session = request.getSession();
        // 存入会话
        session.setAttribute("info", mainInfo);
        // 转发到main.jsp
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        //System.out.println("session:"+session.getAttribute("indexparam")+","+session.getAttribute("typeparam"));
        return "main";
    }
}
