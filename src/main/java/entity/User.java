package entity;

public class User {

    private int oId;// '����',
    private String userEmail;// '�û�����',
    private String userName;//'�û���',
    private String userURL;// '�û����ӵ�ַ',
    private String userPassword;// '�û����룬MD5',
    private String userRole;//'�û���ɫ������Ա��adminRole����ͨ�û���defaultRole���ÿ��û���visitorRole',
    private int userArticleCount;// '�û����¼���',
    private int userPublishedArticleCount;// '�û��ѷ������¼���',
    private String userAvatar;// '�û�ͷ��ͼƬ���ӵ�ַ',

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
