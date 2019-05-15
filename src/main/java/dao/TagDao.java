package dao;

import entity.Tag;
import util.DBUtils;
import util.PageInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �������жԱ�ǩ������ݿ����
 *
 * @author Tedu
 */
public class TagDao {
    // ��װtagʵ��
    private Tag mapTag(ResultSet rs) throws SQLException {
        Tag t = null;
        int oId = rs.getInt(1);
        int tagPublishedRefCount = rs.getInt(2);
        int tagReferenceCount = rs.getInt(3);
        String tagTitle = rs.getString(4);
        t = new Tag(oId, tagPublishedRefCount, tagReferenceCount, tagTitle);
        return t;
    }

    // ��ñ�ǩ����
    public int getTagCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_tag";
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

    // ���ݱ�ǩ����ѯ��ǩ
    public Tag getTagByTagName(String ts) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "oId,tagPublishedRefCount,tagReferenceCount,tagTitle "
                    + "FROM "
                    + "blogs_tag "
                    + "WHERE "
                    + "tagTitle=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ts);
            ResultSet rs = ps.executeQuery();
            Tag t = null;
            while (rs.next()) {
                t = mapTag(rs);
            }
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ����һ����ǩ�����������ǩ��id
    public int addTag(String ts) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "INSERT INTO "
                    + "blogs_tag "
                    + "VALUES"
                    + "(NULL,0,0,?)";
            // ����������ǩ��id ��Ҫ��дStatement.RETURN_GENERATED_KEYS
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ts);
            ps.executeUpdate();
            // ִ����������������ǩ��id
            ResultSet rs = ps.getGeneratedKeys();
            int tagid = 0;
            while (rs.next()) {
                tagid = rs.getInt(1);
            }
            // �������id
            return tagid;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // �������ºͱ�ǩ�Ĺ�ϵ��
    public int addArticleTag(int aid, int tid) {
        try (Connection conn = DBUtils.getConn()) {
            String insertsql = "INSERT INTO "
                    + "blogs_tag_article "
                    + "VALUES"
                    + "(?,?)";
            // ��ִ�����ºͱ�ǩ��ϵ�Ľ���
            PreparedStatement ps = conn.prepareStatement(insertsql);
            ps.setInt(1, aid);
            ps.setInt(2, tid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��ѯ���б�ǩ
    public List<Tag> getTags() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "oId,tagPublishedRefCount,tagReferenceCount,tagTitle  "
                    + "FROM "
                    + "blogs_tag";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Tag> list = new ArrayList<Tag>();
            while (rs.next()) {
                Tag t = mapTag(rs);
                list.add(t);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��ѯ��ĳһƪ���µ����б�ǩ
    public List<Tag> getTagsByArticleId(int aid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "bt.oId,bt.tagPublishedRefCount,"
                    + "bt.tagReferenceCount,bt.tagTitle "
                    + "FROM  "
                    + "blogs_tag_article bta  INNER JOIN  blogs_tag bt "
                    + "ON "
                    + "bt.oid=bta.tag_oid  "
                    + "WHERE "
                    + "bta.article_oid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, aid);
            ResultSet rs = ps.executeQuery();
            List<Tag> list = new ArrayList<Tag>();
            while (rs.next()) {
                Tag t = mapTag(rs);
                list.add(t);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // �����б�ǩ��װ��map�еķ���
    public Map<String, Integer> getTagsMap() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "SELECT "
                    + "oid,tagtitle "
                    + "FROM "
                    + "blogs_tag";
            Map<String, Integer> map = new HashMap<String, Integer>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(2), rs.getInt(1));
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ɾ����ǩ�����µĹ�ϵ
    public int deleteTagRalationById(int tid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_tag_article "
                    + "WHERE "
                    + "tag_oId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ɾ����ǩ
    public int deleteTagById(int tid) {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "DELETE "
                    + "FROM "
                    + "blogs_tag "
                    + "WHERE "
                    + "oId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tid);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ��ҳ��ѯ��ǩ
    public List<Tag> getTags(PageInfo pi) {
        try (Connection conn = DBUtils.getConn()) {
            //��ѯ������
            int count = getTagCount();
            // setCountΪ��������ֵ�����Զ�������ҳ��
            pi.setCount(count);
            // ��ֹ��ѯ����ҳ��
            if (pi.getPageNum() > pi.getTotal())
                pi.setPageNum(pi.getTotal());
            if (pi.getPageNum() < 1)
                pi.setPageNum(1);
            String sql = "SELECT "
                    + "oId,tagPublishedRefCount,tagReferenceCount,tagTitle "
                    + "FROM "
                    + "blogs_tag "
                    + "limit ?,?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (pi.getPageNum() - 1) * pi.getPageSize());
            ps.setInt(2, pi.getPageSize());
            ResultSet rs = ps.executeQuery();
            List<Tag> list = new ArrayList<Tag>();
            while (rs.next()) {
                Tag t = mapTag(rs);
                list.add(t);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // ά����ǩ������
    public int updateTagRefCount() {
        try (Connection conn = DBUtils.getConn()) {
            String sql = "UPDATE "
                    + "blogs_tag tag  "
                    + "SET  "
                    + "tag.tagReferenceCount="
                    + "(SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_article ba INNER JOIN blogs_tag_article bta "
                    + "ON "
                    + "ba.oid = bta.article_oId "
                    + "WHERE  "
                    + "bta.tag_oId=tag.oid),"
                    + "tag.tagPublishedRefCount="
                    + "(SELECT "
                    + "COUNT(1) "
                    + "FROM "
                    + "blogs_article ba INNER JOIN blogs_tag_article bta "
                    + "ON "
                    + "ba.oid = bta.article_oId "
                    + "WHERE "
                    + "ba.articleIsPublished=1 "
                    + "AND  "
                    + "bta.tag_oId=tag.oid)";

            PreparedStatement ps = conn.prepareStatement(sql);
            int num = ps.executeUpdate();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
