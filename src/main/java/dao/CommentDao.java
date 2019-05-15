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
 * 处理所有对评论类的数据库操作
 *
 * @author Tedu
 */
public class CommentDao {
    // 封装评论对象
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
        // 本条评论
        com = new Comment(oId, commentContent, commentCreated);
        // 本条评论的用户
        User u = new User();
        u.setUserName(commentName);
        u.setoId(commentUserId);
        u.setUserAvatar(commentThumbnailURL);
        // 评论的文章
        Article article = new Article();
        article.setoId(commentOnId);
        article.setArticleTitle(articletitle);
        // 以下是回复评论的
        // 回复评论的用户
        User uu = new User();
        uu.setUserName(commentOriginalCommentName);
        // 回复评论
        Comment cc = new Comment();
        cc.setoId(commentOriginalCommentId);
        // 组合过程
        cc.setUser(uu);
        com.setArticle(article);
        com.setUser(u);
        com.setComment(cc);
        return com;
    }

    // 查询评论数
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

    // 删除评论
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

    // 分页查询所有评论
    public List<Comment> getComment(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            // 执行查询总条数
            int count = getCommentCount();
            // setCount为总条数赋值，会自动计算总页数
            pi.setCount(count);
            // 防止查询超限页数
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

    // 根据文章id删除所有评论
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

    // 根据用戶id删除所有评论
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
