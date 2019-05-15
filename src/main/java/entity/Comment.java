package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

//评论
public class Comment {

    private int oId;// '主键',
    private String commentContent;// '评论内容',
    private long commentCreated;// '评论时间戳',
    private Article article;// '评论的文章/页面,
    private Comment comment;// '评论回复时原始的评论 即父评论 ',
    private User user;// 评论人
    private String commentCreatedStr;

    public Comment() {
    }

    public Comment(int oId, String commentContent, long commentCreated) {
        super();
        this.oId = oId;
        this.commentContent = commentContent;
        this.commentCreated = commentCreated;
    }

    public Comment(int oId, String commentContent, long commentCreated, Article article, Comment comment, User user) {
        super();
        this.oId = oId;
        this.commentContent = commentContent;
        this.commentCreated = commentCreated;
        this.article = article;
        this.comment = comment;
        this.user = user;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public long getCommentCreated() {
        return commentCreated;
    }

    public void setCommentCreated(long commentCreated) {
        this.commentCreated = commentCreated;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentCreatedStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(this.commentCreated);
        return sdf.format(d);
    }
}
