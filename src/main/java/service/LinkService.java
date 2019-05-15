package service;

import dao.LinkDao;
import entity.Link;
import util.PageInfo;

import java.util.List;

public class LinkService {
    private LinkDao linkDao;

    public LinkDao getLinkDao() {
        return linkDao;
    }

    public void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    // 删除链接
    public int deleteLinkById(int lid) {
        return linkDao.deleteLinkById(lid);

    }

    // 分页查询所有链接
    public List<Link> getLinks(PageInfo pi) {
        return linkDao.getLinks(pi);
    }

    // 增加新链接
    public int addLink(Link l) {
        return linkDao.addLink(l);
    }
}
