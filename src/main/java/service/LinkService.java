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

    // ɾ������
    public int deleteLinkById(int lid) {
        return linkDao.deleteLinkById(lid);

    }

    // ��ҳ��ѯ��������
    public List<Link> getLinks(PageInfo pi) {
        return linkDao.getLinks(pi);
    }

    // ����������
    public int addLink(Link l) {
        return linkDao.addLink(l);
    }
}
