package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * �������в�������ģ������Ŀ�����
 *
 * @author Tedu
 */
public class OptionController {
    // ��ʾ��������
    @RequestMapping("/toManageOption.do")
    public String toManageOption(HttpServletRequest request, Context ctx) {
        // �ü�ֵ�Եķ�ʽ��ø��ֲ���������Ϣ
        Map<String, String> map = BlogsContext.getInstance().getOptionService().getOptions();
        HttpSession session = request.getSession();
        // ���õ�������
        session.setAttribute("indexparam", "bk");
        session.setAttribute("typeparam", "bk");
        //����Thymeleaf������
        ctx.setVariable("option", map);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "option/list";
    }

    // �޸Ĳ�������
    @RequestMapping("/toUpdateOption.do")
    public String toUpdateOption(HttpServletRequest request) throws UnsupportedEncodingException {
        // ����������
        request.setCharacterEncoding("utf-8");
        Map<String, String> map = new HashMap<String, String>();
        // ��ò����޸ĺ������
        map.put("blogname", request.getParameter("blogName"));
        map.put("blogvalue", request.getParameter("blogWelcome"));
        // �޸�����
        BlogsContext.getInstance().getOptionService().updateOption(map);
        return "redirect:toManageOption.do";
    }
}
