package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import entity.User;
import org.thymeleaf.context.Context;
import util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 处理所有用户模块请求的控制器
 *
 * @author Tedu
 */
public class UserController {
    // 删除用户/分页查询用户
    @RequestMapping("/toManageUser.do")
    public String toManageUser(HttpServletRequest request, Context ctx) {
        // 如果是删除用户的命令
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getUserService().deleteUserById(Integer.parseInt(sid));
        }
        // 分页条件
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 8;
        // 分页的条件对象
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        List<User> list = BlogsContext.getInstance().getUserService().getUsers(pi);
        HttpSession session = request.getSession();
        // 设置导航焦点
        session.setAttribute("indexparam", "yh");
        session.setAttribute("typeparam", "yp");
        ctx.setVariable("users", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "user/list";
    }
}
