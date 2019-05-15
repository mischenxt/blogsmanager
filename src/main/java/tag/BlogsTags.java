package tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogsTags extends TagSupport {

    private static final long serialVersionUID = 1L;
    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    // ��ʱ��ƫ����ת����������
    @Override
    public int doStartTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        // �ƶ����ڸ�ʽ
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // ת�������
            out.println(sdf.format(new Date(date)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
