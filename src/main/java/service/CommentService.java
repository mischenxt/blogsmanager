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

    // ɾ������
    public int deleteCommentById(int oid) {
        // ɾ������������ۺͻظ�������۵���������
        int num = commentDao.deleteCommentById(oid);
        // ά������������
        BlogsContext.getInstance().getArticleDao().updateArticleCommentRelation();
        return num;
    }

    // ��ҳ��ѯ��������
    public List<Comment> getComment(PageInfo pi) {
        return commentDao.getComment(pi);
    }
}
