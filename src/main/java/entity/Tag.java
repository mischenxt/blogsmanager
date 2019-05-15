package entity;

import java.util.ArrayList;
import java.util.List;

//标签
public class Tag {

    private int oId;// '主键',
    private int tagPublishedRefCount;// '标签关联的已发布文章计数',
    private int tagReferenceCount;// '标签关联的文章计数',
    private String tagTitle;// '标签标题',

    private List<Article> articles = new ArrayList<Article>();

    public Tag() {
    }

    public Tag(int oId, int tagPublishedRefCount, int tagReferenceCount, String tagTitle) {
        super();
        this.oId = oId;
        this.tagPublishedRefCount = tagPublishedRefCount;
        this.tagReferenceCount = tagReferenceCount;
        this.tagTitle = tagTitle;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public int getTagPublishedRefCount() {
        return tagPublishedRefCount;
    }

    public void setTagPublishedRefCount(int tagPublishedRefCount) {
        this.tagPublishedRefCount = tagPublishedRefCount;
    }

    public int getTagReferenceCount() {
        return tagReferenceCount;
    }

    public void setTagReferenceCount(int tagReferenceCount) {
        this.tagReferenceCount = tagReferenceCount;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}