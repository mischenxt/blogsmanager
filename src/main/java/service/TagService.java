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

    // 查询所有标签
    public List<Tag> getTags() {
        return tagDao.getTags();
    }

    // 按指定id删除标签
    public int deleteTagById(int tid) {
        // 先删除关联关系
        @SuppressWarnings("unused")
        int num = tagDao.deleteTagRalationById(tid);
        // 再删除标签
        int nn = tagDao.deleteTagById(tid);
        // 从map中删除标签
        Map<String, Integer> m = BlogsContext.getInstance().getArticleService().getMap();
        for (Entry<String, Integer> en : m.entrySet()) {
            if (en.getValue() == tid) {
                m.remove(en.getKey());
                break;
            }
        }
        return nn;
    }

    // 查询所有标签
    public List<Tag> getTags(PageInfo pi) {
        return tagDao.getTags(pi);
    }

    // 新增标签
    public int addTag(String tagTitle) {
        // 先从标签集合中查制定标签的id
        Integer tid = BlogsContext.getInstance().getArticleService().getMap().get(tagTitle);
        // 如果这个id是空，增加标签
        int num = tid == null ? tagDao.addTag(tagTitle) : 0;
        // 上面如果是空才会返回>0的num
        // 所有新增的标签进if，如果是已存在的标签直接返回标签id
        if (num > 0) {
            // 将新增的标签放入标签集合
            BlogsContext.getInstance().getArticleService().getMap().put(tagTitle, num);
        }
        return num;
    }
}
