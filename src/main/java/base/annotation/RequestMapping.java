package base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//ע�����������ʱ��Ч
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    //ע�������ע������ֵ��"value"����������������ֵʱĬ��Ϊvalue��ֵ��
    public String value();

}
