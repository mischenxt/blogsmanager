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
 * ���������û�ģ������Ŀ�����
 *
 * @author Tedu
 */
public class UserController {
    // ɾ���û�/��ҳ��ѯ�û�
    @RequestMapping("/toManageUser.do")
    public String toManageUser(HttpServletRequest request, Context ctx) {
        // �����ɾ���û�������
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getUserService().deleteUserById(Integer.parseInt(sid));
        }
        // ��ҳ����
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 8;
        // ��ҳ����������
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        List<User> list = BlogsContext.getInstance().getUserService().getUsers(pi);
        HttpSession session = request.getSession();
        // ���õ�������
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
