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
 * ������������ģ������Ŀ�����
 *
 * @author Tedu
 */
public class ArticleController {
    // ���ʷ������µ�ҳ��
    @RequestMapping("/toSendArticle.do")
    public String toSendArticle(HttpServletRequest request, Context ctx) {
        // ��ѯ���б�ǩ
        List<Tag> list = BlogsContext.getInstance().getTagService().getTags();
        HttpSession session = request.getSession();
        // ���õ�������
        session.setAttribute("indexparam", "fb");
        session.setAttribute("typeparam", "wz");
        //����Thymeleaf������
        ctx.setVariable("tags", list);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "article/send";
    }

    // ��ҳ��ѯ����
    @RequestMapping("/toManageArticle.do")
    public String toManageArticle(HttpServletRequest request, Context ctx) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // ����Ƿ���ɾ������
        deleteArticle(request);
        // ����Ƿ����ö�����
        toggleTop(request);
        // ��ò�ѯ����
        String title = request.getParameter("articleTitle");
        // �Ƿ��ѯ�ݸ�
        String strPublished = request.getParameter("articleIsPublished");
        int isPublished = 1;// Ĭ��1
        // ���strPublished���ǿգ��Ͱ�strPublished��ֵ
        if (strPublished != null)
            isPublished = Integer.parseInt(strPublished);
        // ��ҳ����
        int pagenum = 1;
        if (request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            pagenum = Integer.parseInt(request.getParameter("page"));
        }
        int pagesize = 8;
        PageInfo pi = new PageInfo();
        pi.setPageNum(pagenum);
        pi.setPageSize(pagesize);
        // ��ѯ��������
        List<Article> list = BlogsContext.getInstance().getArticleService().getArticles(title, pi, isPublished);
        HttpSession session = request.getSession();
        // ���õ�������
        session.setAttribute("indexparam", (isPublished == 1 ? "wz" : "cg"));
        session.setAttribute("typeparam", "wz");
        //����Thymeleaf������
        ctx.setVariable("articleTitle", title);
        ctx.setVariable("articles", list);
        ctx.setVariable("pi", pi);
        ctx.setVariable("indexparam", session.getAttribute("indexparam"));
        ctx.setVariable("typeparam", session.getAttribute("typeparam"));
        ctx.setVariable("user", session.getAttribute("user"));
        ctx.setVariable("info", session.getAttribute("info"));
        return "article/list";
    }

    // ��������
    @RequestMapping("/addArticle.do")
    public String addArticle(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // ����û��������µ���Ϣ
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
        // ����ҵ��㷽���������ı�ǩ������ʱ���ַ���
        @SuppressWarnings("unused")
        int num = BlogsContext.getInstance().getArticleService().addArticle(article, tag);
        return "redirect:main.do";
    }

    // �༭����֮ǰ,��ѯһƪ����
    @RequestMapping("/toEditArticle.do")
    public String toEditArticle(HttpServletRequest request, Context ctx) {
        // ���Ҫ�༭���µ�id
        int oid = Integer.parseInt(request.getParameter("oid"));
        // ��ѯ������Ϣ
        Article article = BlogsContext.getInstance().getArticleService().getArticlesById(oid);
        // ��ѯ���б�ǩ
        List<Tag> list = BlogsContext.getInstance().getTagService().getTags();
        //�����µı�ǩת�����ַ����Ա���ʾ
        StringBuffer buffer = new StringBuffer();
        for (Tag tag : article.getTags()) {
            buffer.append(tag.getTagTitle() + ",");
        }

        // ����Thymeleaf������
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

    // �༭����
    @RequestMapping("/editArticle.do")
    public String editArticle(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        // ����û��������µ���Ϣ
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
        // ����ҵ��㷽���������ı�ǩ������ʱ���ַ���
        @SuppressWarnings("unused")
        int num = BlogsContext.getInstance().getArticleService().editArticle(article, tag);
        return "redirect:main.do";
    }

    // ɾ������
    public void deleteArticle(HttpServletRequest request) {
        // ���id
        String sid = request.getParameter("delid");
        // ���id������ɾ��
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getArticleService().deleteArticle(Integer.parseInt(sid));
        }
    }

    // �ö�\ȡ���ö�����
    public void toggleTop(HttpServletRequest request) {
        // ���id
        String sid = request.getParameter("topid");
        // ���id���ڸı��ö�״̬
        if (sid != null && sid.length() > 0) {
            BlogsContext.getInstance().getArticleService().toggleTop(Integer.parseInt(sid));
        }
    }
}