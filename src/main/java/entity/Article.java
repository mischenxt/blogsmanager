package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章
public class Article {

    private int oId; // '主键',
    private String articleTitle; // '文章标题',
    private String articleAbstract; // '文章摘要',
    private int articleCommentCount; // '文章评论计数',
    private int articleViewCount; // '文章浏览计数',
    private String articleContent; // '文章正文内容',
    private int articleHadBeenPublished; // '文章是否已经发布过',
    private int articleIsPublished; // '文章是否处于已发布状态',
    private int articlePutTop; // '文章是否置顶',
    private long articleCreated; // '文章创建时间戳',
    private long articleUpdated; // '文章更新时间戳',
    private double articleRandomDouble; // '文章随机数，用于快速查询随机文章列表',
    private int articleCommentable; // '文章是否可以评论',
    private String articleCreatedStr; // '文章创建时间戳字符串',
    private String articleUpdatedStr; //'文章更新时间戳字符串',

    private User user;// '文章作者 id',

    private List<Tag> tags = new ArrayList<Tag>();

    public Article() {
    }

    public Article(int oId, String articleTitle, String articleAbstract, int articleCommentCount, int articleViewCount,
                   String articleContent, int articleHadBeenPublished, int articleIsPublished, int articlePutTop,
                   long articleCreated, long articleUpdated, double articleRandomDouble, int articleCommentable) {
        super();
        this.oId = oId;
        this.articleTitle = articleTitle;
        this.articleAbstract = articleAbstract;
        this.articleCommentCount = articleCommentCount;
        this.articleViewCount = articleViewCount;
        this.articleContent = articleContent;
        this.articleHadBeenPublished = articleHadBeenPublished;
        this.articleIsPublished = articleIsPublished;
        this.articlePutTop = articlePutTop;
        this.articleCreated = articleCreated;
        this.articleUpdated = articleUpdated;
        this.articleRandomDouble = articleRandomDouble;
        this.articleCommentable = articleCommentable;
    }

    public Article(int oId, String articleTitle, String articleAbstract, int articleCommentCount, int articleViewCount,
                   String articleContent, int articleHadBeenPublished, int articleIsPublished, int articlePutTop,
                   long articleCreated, long articleUpdated, double articleRandomDouble, int articleCommentable, User user) {
        super();
        this.oId = oId;
        this.articleTitle = articleTitle;
        this.articleAbstract = articleAbstract;
        this.articleCommentCount = articleCommentCount;
        this.articleViewCount = articleViewCount;
        this.articleContent = articleContent;
        this.articleHadBeenPublished = articleHadBeenPublished;
        this.articleIsPublished = articleIsPublished;
        this.articlePutTop = articlePutTop;
        this.articleCreated = articleCreated;
        this.articleUpdated = articleUpdated;
        this.articleRandomDouble = articleRandomDouble;
        this.articleCommentable = articleCommentable;
        this.user = user;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public int getArticleCommentCount() {
        return articleCommentCount;
    }

    public void setArticleCommentCount(int articleCommentCount) {
        this.articleCommentCount = articleCommentCount;
    }

    public int getArticleViewCount() {
        return articleViewCount;
    }

    public void setArticleViewCount(int articleViewCount) {
        this.articleViewCount = articleViewCount;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public int getArticleHadBeenPublished() {
        return articleHadBeenPublished;
    }

    public void setArticleHadBeenPublished(int articleHadBeenPublished) {
        this.articleHadBeenPublished = articleHadBeenPublished;
    }

    public int getArticleIsPublished() {
        return articleIsPublished;
    }

    public void setArticleIsPublished(int articleIsPublished) {
        this.articleIsPublished = articleIsPublished;
    }

    public int getArticlePutTop() {
        return articlePutTop;
    }

    public void setArticlePutTop(int articlePutTop) {
        this.articlePutTop = articlePutTop;
    }

    public long getArticleCreated() {
        return articleCreated;
    }

    public void setArticleCreated(long articleCreated) {
        this.articleCreated = articleCreated;
    }

    public long getArticleUpdated() {
        return articleUpdated;
    }

    public void setArticleUpdated(long articleUpdated) {
        this.articleUpdated = articleUpdated;
    }

    public double getArticleRandomDouble() {
        return articleRandomDouble;
    }

    public void setArticleRandomDouble(double articleRandomDouble) {
        this.articleRandomDouble = articleRandomDouble;
    }

    public int getArticleCommentable() {
        return articleCommentable;
    }

    public void setArticleCommentable(int articleCommentable) {
        this.articleCommentable = articleCommentable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getArticleCreatedStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(this.articleCreated);
        return sdf.format(d);
    }

    public String getArticleUpdatedStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(this.articleUpdated);
        return sdf.format(d);
    }

}