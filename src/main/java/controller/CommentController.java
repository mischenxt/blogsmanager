package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import entity.Comment;
import org.thymeleaf.context.Context;
import util.PageInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * ������������ģ������Ŀ�����
 *
 * @author Tedu
 */
public class CommentController extends HttpServlet {
    //ɾ������/��ҳ��ѯ����
    @RequestMapping("/toManageComment.do")
    public String toManageComment(HttpServletRequest request, Context ctx) {
        // �����ɾ�����۵�����
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getCommentService().deleteCommentById(Integer.parseInt(sid));
        }
        // ��ҳ����
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 6;
        // ��ҳ����������
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        List<Comment> list = BlogsContext.getInstance().getCommentService().getComment(pi);
        HttpSession session = request.getSession();
        //���õ�������
        session.setAttribute("indexparam", "pl");
        session.setAttribute("typeparam", "yp");
        //����Thymeleaf������
        ctx.setVariable("coms", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", "pl");
        ctx.setVariable("typeparam", "yp");
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "comment/list";
    }
}