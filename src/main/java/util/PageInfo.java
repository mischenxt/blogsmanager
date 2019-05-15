package util;

public class PageInfo {

    private Integer pageSize;// ÿҳ����
    private Integer pageNum;// ��ǰҳ��
    private Integer count;// ������
    private Integer total;// ��ҳ��

    public PageInfo(Integer pageSize, Integer pageNum, Integer count, Integer total) {
        super();
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.count = count;
        this.total = total;
    }

    public PageInfo() {
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
        // ��Ϊ��������ֵʱ�Զ�������ҳ��
        total = (count + pageSize - 1) / pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
