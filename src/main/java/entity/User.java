package entity;

public class User {

    private int oId;// '主键',
    private String userEmail;// '用户邮箱',
    private String userName;//'用户名',
    private String userURL;// '用户链接地址',
    private String userPassword;// '用户密码，MD5',
    private String userRole;//'用户角色，管理员：adminRole，普通用户：defaultRole，访客用户：visitorRole',
    private int userArticleCount;// '用户文章计数',
    private int userPublishedArticleCount;// '用户已发布文章计数',
    private String userAvatar;// '用户头像图片链接地址',

    public User() {
    }

    public User(int oId, String userEmail, String userName, String userURL, String userPassword, String userRole,
                int userArticleCount, int userPublishedArticleCount, String userAvatar) {
        super();
        this.oId = oId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userURL = userURL;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userArticleCount = userArticleCount;
        this.userPublishedArticleCount = userPublishedArticleCount;
        this.userAvatar = userAvatar;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserURL() {
        return userURL;
    }

    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getUserArticleCount() {
        return userArticleCount;
    }

    public void setUserArticleCount(int userArticleCount) {
        this.userArticleCount = userArticleCount;
    }

    public int getUserPublishedArticleCount() {
        return userPublishedArticleCount;
    }

    public void setUserPublishedArticleCount(int userPublishedArticleCount) {
        this.userPublishedArticleCount = userPublishedArticleCount;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
