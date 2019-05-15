package base.context;

import dao.*;
import service.*;

/**
 * 这个类包含所有业务逻辑层对象和数据访问层对象
 * 用于控制反转，将所有类的对象控制在这一个类中
 * 这个类是单例的
 * 需要业务逻辑或数据访问对象时从这个类中获得即可
 * 这个类中的业务逻辑对象中的数据访问对象是已经被赋值的
 */
public class BlogsContext {
    // 单例对象
    private static BlogsContext blogsContext;
    private UserDao userDao;
    private UserService userService;
    private ArticleDao articleDao;
    private ArticleService articleService;
    private CommentDao commentDao;
    private CommentService commentService;
    private LinkDao linkDao;
    private LinkService linkService;
    private OptionDao optionDao;
    private OptionService optionService;
    private TagDao tagDao;
    private TagService tagService;

    //私有构造构成单例
    private BlogsContext() {
        //实例化所有对象
        userDao = new UserDao();
        userService = new UserService();
        articleDao = new ArticleDao();
        articleService = new ArticleService();
        commentDao = new CommentDao();
        commentService = new CommentService();
        linkDao = new LinkDao();
        linkService = new LinkService();
        optionDao = new OptionDao();
        optionService = new OptionService();
        tagDao = new TagDao();
        tagService = new TagService();
        //将数据访问对象注入业务逻辑对象
        tagService.setTagDao(tagDao);
        userService.setUserDao(userDao);
        optionService.setOptionDao(optionDao);
        linkService.setLinkDao(linkDao);
        commentService.setCommentDao(commentDao);
        articleService.setArticleDao(articleDao);
        articleService.setMap(tagDao.getTagsMap());
    }

    //获得单例对象的方法
    public static BlogsContext getInstance() {
        if (blogsContext == null) {
            blogsContext = new BlogsContext();
        }
        return blogsContext;
    }

    //以下是getters
    public UserDao getUserDao() {
        return userDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public LinkDao getLinkDao() {
        return linkDao;
    }

    public LinkService getLinkService() {
        return linkService;
    }

    public OptionDao getOptionDao() {
        return optionDao;
    }

    public OptionService getOptionService() {
        return optionService;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

    public TagService getTagService() {
        return tagService;
    }
}