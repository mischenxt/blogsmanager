package entity;

//��������
public class Link {

    private int oId;//'����',
    private String linkAddress;// '���ӵ�ַ',
    private String linkDescription;// '��������',
    private int linkOrder;// '����չ�ֵ�����',
    private String linkTitle;// '���ӱ���',

    public Link() {
    }

    public Link(int oId, String linkAddress, String linkDescription, int linkOrder, String linkTitle) {
        super();
        this.oId = oId;
        this.linkAddress = linkAddress;
        this.linkDescription = linkDescription;
        this.linkOrder = linkOrder;
        this.linkTitle = linkTitle;
    }

    public int getoId() {
        return oId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public int getLinkOrder() {
        return linkOrder;
    }

    public void setLinkOrder(int linkOrder) {
        this.linkOrder = linkOrder;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }
}
