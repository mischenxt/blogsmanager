package dao;

import base.context.BlogsContext;
import entity.Article;
import entity.Tag;
import util.DBUtils;
import util.PageInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * �������ж�����������ݿ����
 *
 * @author Tedu
 */
public class ArticleDao {
    // ��ѯʵ���װ
    public Article mapArticle(ResultSet rs) throws SQLException {
        //�����������ֵ
        int oId = rs.getInt(1);
        String articleTitle = rs.getString(2);
        String articleAbstract = rs.getString(3);
        int articleCommentCount = rs.getInt(5);
        int articleViewCount = rs.getInt(6);
        String articleContent = rs.getString(7);
        int articleHadBeenPublished = rs.getInt(8);
        int articleIsPublished = rs.getInt(9);
        int articlePutTop = rs.getInt(10);
        long articleCreated = rs.getLong(11);
        long articleUpdated = rs.getLong(12);
        int articleRandomDouble = rs.getInt(13);
        int articleCommentable = rs.getInt(14);
        //����һ�����µ�ʵ����
        Article article = new Article(oId, articleTitle, articleAbstract, articleCommentCount, articleViewCount,
                articleContent, articleHadBeenPublished, articleIsPublished, articlePutTop, articleCreated,
                articleUpdated, articleRandomDouble, articleCommentable);
        return article;
    }

    // ��ѯ������
    public int getArticleCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_article";
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

    // ��������
    public int addArticle(Article article) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "INSERT "
                    + "INTO "
                    + "blogs_article "
                    + "VALUES"
                    + "(NULL,?,?,?,0,0,?,?,?,0,?,?,?,?)";
            // ����������Ҫ����������µ�id�����дStatement.RETURN_GENERATED_KEYS
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, article.getArticleTitle());
            ps.setString(2, article.getArticleAbstract());
            ps.setInt(3, article.getUser().getoId());
            ps.setString(4, article.getArticleContent());
            ps.setInt(5, article.getArticleIsPublished());
            ps.setInt(6, article.getArticleIsPublished());
            // ��ǰʱ�����ݿⱣ�����ʱ��ƫ����
            // ���´���������޸�ʱ�䶼�ǵ�ǰʱ��ƫ����
            // ��õ�ǰʱ��ƫ����
            long dd = System.currentTimeMillis();
            ps.setLong(7, dd);
            ps.setLong(8, dd);
            ps.setInt(9, (int) (Math.random() * 100000));
            ps.setInt(10, article.getArticleCommentable());
            // ִ������
            ps.executeUpdate();
            // ����������µ�����
            ResultSet rs = ps.getGeneratedKeys();
            int num = 0;
            while (rs.next()) {
                num = rs.getInt(1);
                // ��������
            }
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��ѯ����
    public List<Article> getArticles(String title, PageInfo pi, int isPublished) {
        try (Connection conn = DBUtils.getConn()) {
            StringBuffer sql = new StringBuffer();
            // ��ʼ��������
            sql.append(" WHERE articleIsPublished=?");
            if (title != null && title.length() > 0) {
                sql.append(" AND articleTitle LIKE ?");
            }
            // �����������
            // ��ѯ��������������
            int count = getArticlesCount(title, isPublished, sql.toString());
            // setCountΪ��������ֵ�����Զ�������ҳ��
            pi.setCount(count);
            // ��ֹ��ѯ����ҳ��
            if (pi.getPageNum() > pi.getTotal())
                pi.setPageNum(pi.getTotal());
            if (pi.getPageNum() < 1)
                pi.setPageNum(1);
            // ��ҳ������ѯ��sql���
            String sqlpage = "SELECT "
                    + "a.oId,a.articleTitle,a.articleAbstract,a.articleAuthorId,a.articleCommentCount,a.articleViewCount,"
                    + "a.articleContent,a.articleHadBeenPublished,a.articleIsPublished,a.articlePutTop,a.articleCreated,"
                    + "a.articleUpdated,a.articleRandomDouble,a.articleCommentable "
                    + "FROM "
                    + "blogs_article a"
                    + sql.toString()
                    + " ORDER BY "
                    + "articleUpdated DESC "
                    + "LIMIT ?,?";
            // ��ҳ��ѯ�Ĵ���
            PreparedStatement ps = conn.prepareStatement(sqlpage);
            //Ϊ������ֵ
            ps.setInt(1, isPublished);
            int index = 2;
            if (title != null && title.length() > 0) {
                ps.setString(index, "%" + title + "%");
                index++;
            }
            ps.setInt(index, (pi.getPageNum() - 1) * pi.getPageSize());
            ps.setInt(index + 1, pi.getPageSize());
            //ִ�в�ѯ
            ResultSet rs = ps.executeQuery();
            List<Article> list = new ArrayList<Article>();
            while (rs.next()) {
                Article a = mapArticle(rs);
                list.add(a);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //��ѯ����������������
    private int getArticlesCount(String title, int isPublished, String sql) {
        try (Connection conn = DBUtils.getConn()) {
            String sqlcount = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_article"
                    + sql;
            // ִ�в�ѯ������
            PreparedStatement ps = conn.prepareStatement(sqlcount);
            //Ϊ������ֵ
            ps.setInt(1, isPublished);
            if (title != null && title.length() > 0) {
                ps.setString(2, "%" + title + "%");
            }
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ɾ������
    public int deleteArticle(int oid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_article "
                    + "WHERE "
                    + "oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, oid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // �ö�/ȡ���ö�
    public int toggleTop(int oid) {
        // sql����е�if����Ϊ���articlePutTop=1,��Ϊ0�������Ϊ1
        try (Connection conn = DBUtils.getConn()) {
            String sql = "UPDATE "
                    + "blogs_article "
                    + "SET "
                    + "articlePutTop=IF(articlePutTop=1,0,1) "
                    + "WHERE "
                    + "oId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, oid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��id��ѯ��һƪ����,�������µı�ǩ
    public Article getArticlesById(int oid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "a.oId,a.articleTitle,a.articleAbstract,a.articleAuthorId,a.articleCommentCount,a.articleViewCount,"
                    + "a.articleContent,a.articleHadBeenPublished,a.articleIsPublished,a.articlePutTop,a.articleCreated,"
                    + "a.articleUpdated,a.articleRandomDouble,a.articleCommentable "
                    + "FROM "
                    + "blogs_article a "
                    + "WHERE "
                    + "oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, oid);
            System.out.println(sql);
            ResultSet rs = ps.executeQuery();
            Article a = null;
            while (rs.next()) {
                a = mapArticle(rs);
                // ��������£���Ҫ��ѯ������µı�ǩ
                List<Tag> tags = BlogsContext.getInstance().getTagDao().getTagsByArticleId(a.getoId());
                a.setTags(tags);
            }
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ɾ�����ºͱ�ǩ�Ĺ�ϵ
    public int deleteArticleRelation(int oid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_tag_article "
                    + "WHERE "
                    + "article_oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, oid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // �޸�����
    public int editArticle(Article article) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "UPDATE "
                    + "blogs_article "
                    + "SET "
                    + "articleTitle=?,articleAbstract=?,articleContent=?,"
                    + "articleIsPublished=?,articleUpdated=?,articleCommentable=? "
                    + "WHERE "
                    + "oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, article.getArticleTitle());
            ps.setString(2, article.getArticleAbstract());
            ps.setString(3, article.getArticleContent());
            ps.setInt(4, article.getArticleIsPublished());
            ps.setLong(5, System.currentTimeMillis());
            ps.setInt(6, article.getArticleCommentable());
            ps.setInt(7, article.getoId());
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ά�����º����������Ĺ�ϵ
    public int updateArticleCommentRelation() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "UPDATE "
                    + "blogs_article ba "
                    + "SET "
                    + "articleCommentCount="
                    + "(SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_comment "
                    + "WHERE "
                    + "commentOnId=ba.oid)";
            PreparedStatement ps = conn.prepareStatement(sql);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
