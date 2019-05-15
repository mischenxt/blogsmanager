package service;

import base.context.BlogsContext;
import dao.TagDao;
import entity.Tag;
import util.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TagService {

    private TagDao tagDao;

    public TagDao getTagDao() {
        return tagDao;
    }

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    // ��ѯ���б�ǩ
    public List<Tag> getTags() {
        return tagDao.getTags();
    }

    // ��ָ��idɾ����ǩ
    public int deleteTagById(int tid) {
        // ��ɾ��������ϵ
        @SuppressWarnings("unused")
        int num = tagDao.deleteTagRalationById(tid);
        // ��ɾ����ǩ
        int nn = tagDao.deleteTagById(tid);
        // ��map��ɾ����ǩ
        Map<String, Integer> m = BlogsContext.getInstance().getArticleService().getMap();
        for (Entry<String, Integer> en : m.entrySet()) {
            if (en.getValue() == tid) {
                m.remove(en.getKey());
                break;
            }
        }
        return nn;
    }

    // ��ѯ���б�ǩ
    public List<Tag> getTags(PageInfo pi) {
        return tagDao.getTags(pi);
    }

    // ������ǩ
    public int addTag(String tagTitle) {
        // �ȴӱ�ǩ�����в��ƶ���ǩ��id
        Integer tid = BlogsContext.getInstance().getArticleService().getMap().get(tagTitle);
        // ������id�ǿգ����ӱ�ǩ
        int num = tid == null ? tagDao.addTag(tagTitle) : 0;
        // ��������ǿղŻ᷵��>0��num
        // ���������ı�ǩ��if��������Ѵ��ڵı�ǩֱ�ӷ��ر�ǩid
        if (num > 0) {
            // �������ı�ǩ�����ǩ����
            BlogsContext.getInstance().getArticleService().getMap().put(tagTitle, num);
        }
        return num;
    }
}
