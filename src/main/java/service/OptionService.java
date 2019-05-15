package service;

import dao.OptionDao;

import java.util.Map;
import java.util.Map.Entry;

public class OptionService {

    private OptionDao optionDao;

    public OptionDao getOptionDao() {
        return optionDao;
    }

    public void setOptionDao(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    // ��ѯ��������
    public Map<String, String> getOptions() {
        return optionDao.getOptions();
    }

    // �༭��������
    public void updateOption(Map<String, String> map) {
        for (Entry<String, String> en : map.entrySet()) {
            optionDao.updateOption(en);
        }
    }
}
