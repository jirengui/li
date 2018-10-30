package ruanjianbei.sport.mysport.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2018/5/21.
 */

public class PinlunBean {
    private UserIndividualInfoBean userIndividualInfoBean ;
    //context中 0是时间， 1是内容
    private List<String> context = new ArrayList<>();
    private int id;
    private List<RepinlunBean> repinlunBeans = new ArrayList<>();

    public UserIndividualInfoBean getUserIndividualInfoBean() {
        return userIndividualInfoBean;
    }

    public void setUserIndividualInfoBean(UserIndividualInfoBean UserIndividualInfoBean) {
        this.userIndividualInfoBean = UserIndividualInfoBean;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RepinlunBean> getRepinlunBeans() {
        return repinlunBeans;
    }

    public void setRepinlunBeans(List<RepinlunBean> repinlunBeans) {
        this.repinlunBeans = repinlunBeans;
    }
}
