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
 * 处理所有评论模块请求的控制器
 *
 * @author Tedu
 */
public class CommentController extends HttpServlet {
    //删除评论/分页查询评论
    @RequestMapping("/toManageComment.do")
    public String toManageComment(HttpServletRequest request, Context ctx) {
        // 如果是删除评论的命令
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getCommentService().deleteCommentById(Integer.parseInt(sid));
        }
        // 分页条件
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 6;
        // 分页的条件对象
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        List<Comment> list = BlogsContext.getInstance().getCommentService().getComment(pi);
        HttpSession session = request.getSession();
        //设置导航焦点
        session.setAttribute("indexparam", "pl");
        session.setAttribute("typeparam", "yp");
        //放入Thymeleaf上下文
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