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
 * ����������������ģ������Ŀ�����
 *
 * @author Tedu
 */
public class LinkController {
    // ɾ������/��ҳ��ѯ����
    @RequestMapping("/toManageLink.do")
    public String toManageLink(HttpServletRequest request, Context ctx) {
        // �����ɾ�����ӵ�����
        String sid = request.getParameter("delid");
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getLinkService().deleteLinkById(Integer.parseInt(sid));
        }
        // ��ҳ����
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 5;
        // ��ҳ����������
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        List<Link> list = BlogsContext.getInstance().getLinkService().getLinks(pi);
        HttpSession session = request.getSession();
        // ���õ�������
        session.setAttribute("indexparam", "yq");
        session.setAttribute("typeparam", "bl");
        //����Thymeleaf������
        ctx.setVariable("links", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "tag/linklist";
    }

    //������������
    @RequestMapping("/addLink.do")
    public String addLink(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String title = request.getParameter("linkTitle");
        String des = request.getParameter("linkDescription");
        String addr = request.getParameter("linkAddress");
        Link l = new Link(0, addr, des, 0, title);
        //ִ������
        BlogsContext.getInstance().getLinkService().addLink(l);
        return "redirect:toManageLink.do";
    }
}