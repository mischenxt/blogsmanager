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
 * 处理所有博客资料模块请求的控制器
 *
 * @author Tedu
 */
public class OptionController {
    // 显示博客资料
    @RequestMapping("/toManageOption.do")
    public String toManageOption(HttpServletRequest request, Context ctx) {
        // 用键值对的方式获得各种博客资料信息
        Map<String, String> map = BlogsContext.getInstance().getOptionService().getOptions();
        HttpSession session = request.getSession();
        // 设置导航焦点
        session.setAttribute("indexparam", "bk");
        session.setAttribute("typeparam", "bk");
        //放入Thymeleaf上下文
        ctx.setVariable("option", map);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "option/list";
    }

    // 修改博客资料
    @RequestMapping("/toUpdateOption.do")
    public String toUpdateOption(HttpServletRequest request) throws UnsupportedEncodingException {
        // 防中文乱码
        request.setCharacterEncoding("utf-8");
        Map<String, String> map = new HashMap<String, String>();
        // 获得博客修改后的资料
        map.put("blogname", request.getParameter("blogName"));
        map.put("blogvalue", request.getParameter("blogWelcome"));
        // 修改资料
        BlogsContext.getInstance().getOptionService().updateOption(map);
        return "redirect:toManageOption.do";
    }
}
