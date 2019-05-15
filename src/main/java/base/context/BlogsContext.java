package base.context;

import dao.*;
import service.*;

/**
 * ������������ҵ���߼����������ݷ��ʲ����
 * ���ڿ��Ʒ�ת����������Ķ����������һ������
 * ������ǵ�����
 * ��Ҫҵ���߼������ݷ��ʶ���ʱ��������л�ü���
 * ������е�ҵ���߼������е����ݷ��ʶ������Ѿ�����ֵ��
 */
public class BlogsContext {
    // ��������
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

    //˽�й��칹�ɵ���
    private BlogsContext() {
        //ʵ�������ж���
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
        //�����ݷ��ʶ���ע��ҵ���߼�����
        tagService.setTagDao(tagDao);
        userService.setUserDao(userDao);
        optionService.setOptionDao(optionDao);
        linkService.setLinkDao(linkDao);
        commentService.setCommentDao(commentDao);
        articleService.setArticleDao(articleDao);
        articleService.setMap(tagDao.getTagsMap());
    }

    //��õ�������ķ���
    public static BlogsContext getInstance() {
        if (blogsContext == null) {
            blogsContext = new BlogsContext();
        }
        return blogsContext;
    }

    //������getters
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