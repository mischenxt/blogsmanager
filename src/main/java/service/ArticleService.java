package service;

import base.context.BlogsContext;
import dao.ArticleDao;
import entity.Article;
import util.PageInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArticleService {

    // 存放所有标签集合
    private Map<String, Integer> map;
    private ArticleDao articleDao;

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    // 新增文章的业务
    public int addArticle(Article article, String tag) {
        // 增加文章开始
        int aid = BlogsContext.getInstance().getArticleDao().addArticle(article);
        // 文章增加失败就返回
        if (aid == 0)
            return 0;
        // 增加文章结束
        // 维护文章和标签的关系
        updateTagToArticle(tag, aid);
        // 维护标签引用数
        BlogsContext.getInstance().getTagDao().updateTagRefCount();
        return 1;
    }

    // 将用户输入的标签字符串增加到tag表后新增关系的方法
    public void updateTagToArticle(String tag, int aid) {
        // 用户写的用","分割的字符串，提取出所有标签
        if (tag != null) {
            String[] ts = tag.split("\\s*,\\s*");
            // 初始化标签id的int数组
            int[] tids = new int[ts.length];

            // 将提取出的字符串循环向tag表中添加
            for (int i = 0; i < ts.length; i++) {
                // 先检查是不是已经有了这个这个标签
                Integer t = map.get(ts[i]);
                // 如果没有这个标签
                if (t == null && ts[i] != null && ts[i].length() > 0) {
                    // 添加这个标签，返回新标签id
                    int tid = BlogsContext.getInstance().getTagDao().addTag(ts[i]);
                    // 将这个id放入准备好的数组
                    tids[i] = tid;
                    map.put(ts[i], tid);
                } else if (t != null) {
                    // 如果有这个标签，将刚刚查出来的id放入数组
                    tids[i] = t;
                }
            }
            // 使用set集合去掉重复的元素
            Set<Integer> s = new HashSet<Integer>();
            for (int i : tids) {
                s.add(i);
            }
            // 标签完成
            // 将增加的文章和这个文章的标签关联
            for (Integer i : s) {
                BlogsContext.getInstance().getTagDao().addArticleTag(aid, i);
            }
        }
    }

    // 查询所有文章
    public List<Article> getArticles(String title, PageInfo pi, int isPublished) {
        return articleDao.getArticles(title, pi, isPublished);
    }

    // 删除文章
    public int deleteArticle(int oid) {
        // 删除文章前先删除文章和标签关系表中数据
        articleDao.deleteArticleRelation(oid);
        // 删除评论这个文章的所有评论
        BlogsContext.getInstance().getCommentDao().deleteCommentByArticleId(oid);
        // 删除文章
        int num = articleDao.deleteArticle(oid);
        // 维护标签引用数
        BlogsContext.getInstance().getTagDao().updateTagRefCount();
        // 维护品论引用数
        articleDao.updateArticleCommentRelation();
        return num;
    }

    // 置顶/取消置顶
    public int toggleTop(int oid) {
        return articleDao.toggleTop(oid);
    }

    // 根据id查询文章
    public Article getArticlesById(int oid) {
        return articleDao.getArticlesById(oid);
    }

    // 编辑文章
    public int editArticle(Article article, String tag) {
        // 编辑文章开始
        int num = BlogsContext.getInstance().getArticleDao().editArticle(article);
        // 文章编辑失败就返回
        if (num == 0)
            return 0;
        // 编辑文章结束
        // 删除当前文章和所有标签的关关系
        articleDao.deleteArticleRelation(article.getoId());
        // 从新建立文章和标签的关系
        updateTagToArticle(tag, article.getoId());
        // 维护标签引用数
        BlogsContext.getInstance().getTagDao().updateTagRefCount();
        return 0;
    }
}
