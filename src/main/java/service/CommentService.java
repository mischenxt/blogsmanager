package service;

import base.context.BlogsContext;
import dao.CommentDao;
import entity.Comment;
import util.PageInfo;

import java.util.List;

public class CommentService {

    private CommentDao commentDao;

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    // 删除评论
    public int deleteCommentById(int oid) {
        // 删除的是这个评论和回复这个评论的所有评论
        int num = commentDao.deleteCommentById(oid);
        // 维护文章评论数
        BlogsContext.getInstance().getArticleDao().updateArticleCommentRelation();
        return num;
    }

    // 分页查询所有评论
    public List<Comment> getComment(PageInfo pi) {
        return commentDao.getComment(pi);
    }
}
