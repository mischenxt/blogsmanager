package dao;

import entity.Article;
import entity.Comment;
import entity.User;
import util.DBUtils;
import util.PageInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * �������ж�����������ݿ����
 *
 * @author Tedu
 */
public class CommentDao {
    // ��װ���۶���
    public Comment mapComment(ResultSet rs) throws SQLException {
        int oId = rs.getInt(1);
        String commentContent = rs.getString(2);
        long commentCreated = rs.getLong(3);
        String commentName = rs.getString(4);
        int commentOnId = rs.getInt(5);
        int commentUserId = rs.getInt(6);
        String commentThumbnailURL = rs.getString(7);
        int commentOriginalCommentId = rs.getInt(8);
        String commentOriginalCommentName = rs.getString(9);
        String articletitle = rs.getString(10);
        Comment com = null;
        // ��������
        com = new Comment(oId, commentContent, commentCreated);
        // �������۵��û�
        User u = new User();
        u.setUserName(commentName);
        u.setoId(commentUserId);
        u.setUserAvatar(commentThumbnailURL);
        // ���۵�����
        Article article = new Article();
        article.setoId(commentOnId);
        article.setArticleTitle(articletitle);
        // �����ǻظ����۵�
        // �ظ����۵��û�
        User uu = new User();
        uu.setUserName(commentOriginalCommentName);
        // �ظ�����
        Comment cc = new Comment();
        cc.setoId(commentOriginalCommentId);
        // ��Ϲ���
        cc.setUser(uu);
        com.setArticle(article);
        com.setUser(u);
        com.setComment(cc);
        return com;
    }

    // ��ѯ������
    public int getCommentCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_comment";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int num = 0;
            while (rs.next()) {
                num = rs.getInt(1);
            }
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ɾ������
    public int deleteCommentById(int oid) {
        try (Connection conn = DBUtils.getConn()) {
            String delsql = "DELETE "
                    + "FROM "
                    + "blogs_comment "
                    + "WHERE "
                    + "oid=? "
                    + "OR "
                    + "commentOriginalCommentId=?";
            PreparedStatement ps = conn.prepareStatement(delsql);
            ps.setInt(1, oid);
            ps.setInt(2, oid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��ҳ��ѯ��������
    public List<Comment> getComment(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            // ִ�в�ѯ������
            int count = getCommentCount();
            // setCountΪ��������ֵ�����Զ�������ҳ��
            pi.setCount(count);
            // ��ֹ��ѯ����ҳ��
            if (pi.getPageNum() > pi.getTotal())
                pi.setPageNum(pi.getTotal());
            if (pi.getPageNum() < 1)
                pi.setPageNum(1);
            String sql = "SELECT "
                    + "bc.oId,bc.commentContent,bc.commentCreated,bc.commentName,"
                    + "bc.commentOnId,bc.commentUserId,bc.commentThumbnailURL,"
                    + "bc.commentOriginalCommentId,bc.commentOriginalCommentName,ba.articletitle "
                    + "FROM "
                    + "blogs_comment bc INNER JOIN blogs_article ba "
                    + "ON bc.commentonid=ba.oid "
                    + "LIMIT ?,?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (pi.getPageNum() - 1) * pi.getPageSize());
            ps.setInt(2, pi.getPageSize());
            ResultSet rs = ps.executeQuery();
            List<Comment> list = new ArrayList<Comment>();
            while (rs.next()) {
                Comment c = mapComment(rs);
                list.add(c);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��������idɾ����������
    public int deleteCommentByArticleId(int aid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_comment "
                    + "WHERE "
                    + "commentOnId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, aid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // �����Ñ�idɾ����������
    public int deleteCommentByUserId(int uid, String uname) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_comment "
                    + "WHERE "
                    + "commentUserId=? "
                    + "OR "
                    + "commentOriginalCommentName=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            ps.setString(2, uname);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
