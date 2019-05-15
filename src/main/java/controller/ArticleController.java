package controller;

import base.annotation.RequestMapping;
import base.context.BlogsContext;
import entity.Article;
import entity.Tag;
import entity.User;
import org.thymeleaf.context.Context;
import util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 处理所有文章模块请求的控制器
 *
 * @author Tedu
 */
public class ArticleController {
    // 访问发布文章的页面
    @RequestMapping("/toSendArticle.do")
    public String toSendArticle(HttpServletRequest request, Context ctx) {
        // 查询所有标签
        List<Tag> list = BlogsContext.getInstance().getTagService().getTags();
        HttpSession session = request.getSession();
        // 设置导航焦点
        session.setAttribute("indexparam", "fb");
        session.setAttribute("typeparam", "wz");
        //放入Thymeleaf上下文
        ctx.setVariable("tags", list);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "article/send";
    }

    // 分页查询文章
    @RequestMapping("/toManageArticle.do")
    public String toManageArticle(HttpServletRequest request, Context ctx) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // 检查是否是删除操作
        deleteArticle(request);
        // 检查是否是置顶操作
        toggleTop(request);
        // 获得查询条件
        String title = request.getParameter("articleTitle");
        // 是否查询草稿
        String strPublished = request.getParameter("articleIsPublished");
        int isPublished = 1;// 默认1
        // 如果strPublished不是空，就按strPublished赋值
        if (strPublished != null)
            isPublished = Integer.parseInt(strPublished);
        // 分页条件
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 8;
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        // 查询所有文章
        List<Article> list = BlogsContext.getInstance().getArticleService().getArticles(title, pi, isPublished);
        HttpSession session = request.getSession();
        // 设置导航焦点
        session.setAttribute("indexparam", (isPublished == 1 ? "wz" : "cg"));
        session.setAttribute("typeparam", "wz");
        //放入Thymeleaf上下文
        ctx.setVariable("articleTitle", title);
        ctx.setVariable("articles", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "article/list";
    }

    // 发布文章
    @RequestMapping("/addArticle.do")
    public String addArticle(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // 获得用户发布文章的信息
        String title = request.getParameter("articleTitle");
        String content = request.getParameter("articleContent");
        String tag = request.getParameter("articleTag");
        String abs = request.getParameter("articleAbstract");
        String canComment = request.getParameter("canComment");
        String sub = request.getParameter("sub");
        Article article = new Article();
        article.setArticleTitle(title);
        article.setArticleContent(content);
        article.setArticleAbstract(abs);
        article.setArticleCommentable(canComment == null ? 0 : 1);
        article.setArticleIsPublished(Integer.parseInt(sub));
        article.setUser((User) request.getSession().getAttribute("user"));
        // 调用业务层方法，方法的标签参数暂时是字符串
        @SuppressWarnings("unused")
        int num = BlogsContext.getInstance().getArticleService().addArticle(article, tag);
        return "redirect:main.do";
    }

    // 编辑文章之前,查询一篇文章
    @RequestMapping("/toEditArticle.do")
    public String toEditArticle(HttpServletRequest request, Context ctx) {
        // 获得要编辑文章的id
        int oid = Integer.parseInt(request.getParameter("oid"));
        // 查询文章信息
        Article article = BlogsContext.getInstance().getArticleService().getArticlesById(oid);
        // 查询所有标签
        List<Tag> list = BlogsContext.getInstance().getTagService().getTags();
        //将文章的标签转换成字符串以便显示
        StringBuffer buffer = new StringBuffer();
        for (Tag tag : article.getTags()) {
            buffer.append(tag.getTagTitle() + ",");
        }

        // 放入Thymeleaf上下文
        ctx.setVariable("tags", list);
        ctx.setVariable("article", article);
        ctx.setVariable("tagStr", buffer.toString());
        HttpSession session = request.getSession();
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));

        return "article/edit";
    }

    // 编辑文章
    @RequestMapping("/editArticle.do")
    public String editArticle(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // 获得用户发布文章的信息
        int oid = Integer.parseInt(request.getParameter("oid"));
        String title = request.getParameter("articleTitle");
        String content = request.getParameter("articleContent");
        String tag = request.getParameter("articleTag");
        String abs = request.getParameter("articleAbstract");
        String canComment = request.getParameter("canComment");
        String sub = request.getParameter("sub");
        Article article = new Article();
        article.setoId(oid);
        article.setArticleTitle(title);
        article.setArticleContent(content);
        article.setArticleAbstract(abs);
        article.setArticleCommentable(canComment == null ? 0 : 1);
        article.setArticleIsPublished(Integer.parseInt(sub));
        // 调用业务层方法，方法的标签参数暂时是字符串
        @SuppressWarnings("unused")
        int num = BlogsContext.getInstance().getArticleService().editArticle(article, tag);
        return "redirect:main.do";
    }

    // 删除文章
    public void deleteArticle(HttpServletRequest request) {
        // 获得id
        String sid = request.getParameter("delid");
        // 如果id存在则删除
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getArticleService().deleteArticle(Integer.parseInt(sid));
        }
    }

    // 置顶\取消置顶文章
    public void toggleTop(HttpServletRequest request) {
        // 获得id
        String sid = request.getParameter("topid");
        // 如果id存在改变置顶状态
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getArticleService().toggleTop(Integer.parseInt(sid));
        }
    }
}