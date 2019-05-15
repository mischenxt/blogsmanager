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
 * 处理所有对文章类的数据库操作
 *
 * @author Tedu
 */
public class ArticleDao {
    // 查询实体封装
    public Article mapArticle(ResultSet rs) throws SQLException {
        //获得所有属性值
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
        //创建一个文章的实体类
        Article article = new Article(oId, articleTitle, articleAbstract, articleCommentCount, articleViewCount,
                articleContent, articleHadBeenPublished, articleIsPublished, articlePutTop, articleCreated,
                articleUpdated, articleRandomDouble, articleCommentable);
        return article;
    }

    // 查询文章数
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

    // 新增文章
    public int addArticle(Article article) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "INSERT "
                    + "INTO "
                    + "blogs_article "
                    + "VALUES"
                    + "(NULL,?,?,?,0,0,?,?,?,0,?,?,?,?)";
            // 新增文章需要获得新增文章的id，需编写Statement.RETURN_GENERATED_KEYS
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, article.getArticleTitle());
            ps.setString(2, article.getArticleAbstract());
            ps.setInt(3, article.getUser().getoId());
            ps.setString(4, article.getArticleContent());
            ps.setInt(5, article.getArticleIsPublished());
            ps.setInt(6, article.getArticleIsPublished());
            // 当前时间数据库保存的是时间偏移量
            // 文章创建和最后修改时间都是当前时间偏移量
            // 获得当前时间偏移量
            long dd = System.currentTimeMillis();
            ps.setLong(7, dd);
            ps.setLong(8, dd);
            ps.setInt(9, (int) (Math.random() * 100000));
            ps.setInt(10, article.getArticleCommentable());
            // 执行新增
            ps.executeUpdate();
            // 获得新增文章的主键
            ResultSet rs = ps.getGeneratedKeys();
            int num = 0;
            while (rs.next()) {
                num = rs.getInt(1);
                // 返回主键
            }
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 查询文章
    public List<Article> getArticles(String title, PageInfo pi, int isPublished) {
        try (Connection conn = DBUtils.getConn()) {
            StringBuffer sql = new StringBuffer();
            // 开始构建条件
            sql.append(" WHERE articleIsPublished=?");
            if (title != null && title.length() > 0) {
                sql.append(" AND articleTitle LIKE ?");
            }
            // 条件构建完成
            // 查询符合条件的行数
            int count = getArticlesCount(title, isPublished, sql.toString());
            // setCount为总条数赋值，会自动计算总页数
            pi.setCount(count);
            // 防止查询超限页数
            if (pi.getPageNum() > pi.getTotal())
                pi.setPageNum(pi.getTotal());
            if (pi.getPageNum() < 1)
                pi.setPageNum(1);
            // 分页条件查询的sql语句
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
            // 分页查询的代码
            PreparedStatement ps = conn.prepareStatement(sqlpage);
            //为条件赋值
            ps.setInt(1, isPublished);
            int index = 2;
            if (title != null && title.length() > 0) {
                ps.setString(index, "%" + title + "%");
                index++;
            }
            ps.setInt(index, (pi.getPageNum() - 1) * pi.getPageSize());
            ps.setInt(index + 1, pi.getPageSize());
            //执行查询
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

    //查询符合条件的总条数
    private int getArticlesCount(String title, int isPublished, String sql) {
        try (Connection conn = DBUtils.getConn()) {
            String sqlcount = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_article"
                    + sql;
            // 执行查询总条数
            PreparedStatement ps = conn.prepareStatement(sqlcount);
            //为条件赋值
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

    // 删除文章
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

    // 置顶/取消置顶
    public int toggleTop(int oid) {
        // sql语句中的if含义为如果articlePutTop=1,改为0，否则改为1
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

    // 按id查询出一篇文章,包含文章的标签
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
                // 查出了文章，还要查询这个文章的标签
                List<Tag> tags = BlogsContext.getInstance().getTagDao().getTagsByArticleId(a.getoId());
                a.setTags(tags);
            }
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // 删除文章和标签的关系
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

    // 修改文章
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

    // 维护文章和评论数量的关系
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
