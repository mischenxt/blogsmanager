package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import entity.Tag;
import org.thymeleaf.context.Context;
import util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 处理所有标签模块请求的控制器
 *
 * @author Tedu
 */
public class TagController {
    // 分页查询标签列表
    @RequestMapping("/toManageTag.do")
    public String toManageTag(HttpServletRequest request, Context ctx) {
        // 如果是删除用户的命令
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getTagService().deleteTagById(Integer.parseInt(sid));
        }
        // 分页条件
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 5;
        // 分页的条件对象
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        List<Tag> list = BlogsContext.getInstance().getTagService().getTags(pi);
        HttpSession session = request.getSession();
        // 设置导航焦点
        session.setAttribute("indexparam", "bq");
        session.setAttribute("typeparam", "bl");
        ctx.setVariable("tags", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "tag/taglist";
    }

    // 新增标签
    @RequestMapping("/addTag.do")
    public String addTag(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String tagTitle = request.getParameter("tagTitle");
        @SuppressWarnings("unused")
        int num = BlogsContext.getInstance().getTagService().addTag(tagTitle);
        return "redirect:toManageTag.do";
    }
}
