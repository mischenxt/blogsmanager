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

    // ������б�ǩ����
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

    // �������µ�ҵ��
    public int addArticle(Article article, String tag) {
        // �������¿�ʼ
        int aid = BlogsContext.getInstance().getArticleDao().addArticle(article);
        // ��������ʧ�ܾͷ���
        if (aid == 0)
            return 0;
        // �������½���
        // ά�����ºͱ�ǩ�Ĺ�ϵ
        updateTagToArticle(tag, aid);
        // ά����ǩ������
        BlogsContext.getInstance().getTagDao().updateTagRefCount();
        return 1;
    }

    // ���û�����ı�ǩ�ַ������ӵ�tag���������ϵ�ķ���
    public void updateTagToArticle(String tag, int aid) {
        // �û�д����","�ָ���ַ�������ȡ�����б�ǩ
        if (tag != null) {
            String[] ts = tag.split("\\s*,\\s*");
            // ��ʼ����ǩid��int����
            int[] tids = new int[ts.length];

            // ����ȡ�����ַ���ѭ����tag�������
            for (int i = 0; i < ts.length; i++) {
                // �ȼ���ǲ����Ѿ�������������ǩ
                Integer t = map.get(ts[i]);
                // ���û�������ǩ
                if (t == null && ts[i] != null && ts[i].length() > 0) {
                    // ��������ǩ�������±�ǩid
                    int tid = BlogsContext.getInstance().getTagDao().addTag(ts[i]);
                    // �����id����׼���õ�����
                    tids[i] = tid;
                    map.put(ts[i], tid);
                } else if (t != null) {
                    // ����������ǩ�����ող������id��������
                    tids[i] = t;
                }
            }
            // ʹ��set����ȥ���ظ���Ԫ��
            Set<Integer> s = new HashSet<Integer>();
            for (int i : tids) {
                s.add(i);
            }
            // ��ǩ���
            // �����ӵ����º�������µı�ǩ����
            for (Integer i : s) {
                BlogsContext.getInstance().getTagDao().addArticleTag(aid, i);
            }
        }
    }

    // ��ѯ��������
    public List<Article> getArticles(String title, PageInfo pi, int isPublished) {
        return articleDao.getArticles(title, pi, isPublished);
    }

    // ɾ������
    public int deleteArticle(int oid) {
        // ɾ������ǰ��ɾ�����ºͱ�ǩ��ϵ��������
        articleDao.deleteArticleRelation(oid);
        // ɾ������������µ���������
        BlogsContext.getInstance().getCommentDao().deleteCommentByArticleId(oid);
        // ɾ������
        int num = articleDao.deleteArticle(oid);
        // ά����ǩ������
        BlogsContext.getInstance().getTagDao().updateTagRefCount();
        // ά��Ʒ��������
        articleDao.updateArticleCommentRelation();
        return num;
    }

    // �ö�/ȡ���ö�
    public int toggleTop(int oid) {
        return articleDao.toggleTop(oid);
    }

    // ����id��ѯ����
    public Article getArticlesById(int oid) {
        return articleDao.getArticlesById(oid);
    }

    // �༭����
    public int editArticle(Article article, String tag) {
        // �༭���¿�ʼ
        int num = BlogsContext.getInstance().getArticleDao().editArticle(article);
        // ���±༭ʧ�ܾͷ���
        if (num == 0)
            return 0;
        // �༭���½���
        // ɾ����ǰ���º����б�ǩ�Ĺع�ϵ
        articleDao.deleteArticleRelation(article.getoId());
        // ���½������ºͱ�ǩ�Ĺ�ϵ
        updateTagToArticle(tag, article.getoId());
        // ά����ǩ������
        BlogsContext.getInstance().getTagDao().updateTagRefCount();
        return 0;
    }
}
