package util;

public class PageInfo {

    private Integer pageSize;// 每页条数
    private Integer pageNum;// 当前页码
    private Integer count;// 总条数
    private Integer total;// 总页数

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
        // 在为总条数赋值时自动计算总页数
        total = (count + pageSize - 1) / pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
