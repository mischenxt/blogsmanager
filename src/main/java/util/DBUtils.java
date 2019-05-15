package util;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtils {
    private static BasicDataSource dataSource;

    static {
        Properties prop = new Properties();
        // �õ��ļ���
        InputStream ips = DBUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            prop.load(ips);
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String initSize = prop.getProperty("initSize");
            //String maxSize = prop.getProperty("maxSize");
            dataSource = new BasicDataSource();
            // �������������Ӳ���
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            // ���ó�ʼ�������� �� �����������
            dataSource.setInitialSize(Integer.parseInt(initSize));
            // dataSource.setMaxActive(Integer.parseInt(maxSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close(Connection conn, Statement stat, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stat != null) {
                stat.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                // �黹֮ǰ ���Զ��ύ��
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
