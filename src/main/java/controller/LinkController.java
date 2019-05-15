package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import entity.Link;
import org.thymeleaf.context.Context;
import util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 处理所有友情链接模块请求的控制器
 *
 * @author Tedu
 */
public class LinkController {
    // 删除链接/分页查询链接
    @RequestMapping("/toManageLink.do")
    public String toManageLink(HttpServletRequest request, Context ctx) {
        // 如果是删除链接的命令
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getLinkService().deleteLinkById(Integer.parseInt(sid));
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
        List<Link> list = BlogsContext.getInstance().getLinkService().getLinks(pi);
        HttpSession session = request.getSession();
        // 设置导航焦点
        session.setAttribute("indexparam", "yq");
        session.setAttribute("typeparam", "bl");
        //放入Thymeleaf上下文
        ctx.setVariable("links", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "tag/linklist";
    }

    //新增友情链接
    @RequestMapping("/addLink.do")
    public String addLink(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String title = request.getParameter("linkTitle");
        String des = request.getParameter("linkDescription");
        String addr = request.getParameter("linkAddress");
        Link l = new Link(0, addr, des, 0, title);
        //执行新增
        BlogsContext.getInstance().getLinkService().addLink(l);
        return "redirect:toManageLink.do";
    }
}