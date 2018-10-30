package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kochiyasanae on 2018/4/8/008.
 */

public class LiaoTianResultBean implements Serializable {
    private UserIndividualInfoBean UserIndividualInfoBean;
    private List<String> list = new ArrayList<>();
    public LiaoTianResultBean(){
        list = null;
    }
    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public UserIndividualInfoBean getUserIndividualInfoBean() {
        return UserIndividualInfoBean;
    }

    public void setUserIndividualInfoBean(UserIndividualInfoBean UserIndividualInfoBean) {
        this.UserIndividualInfoBean = UserIndividualInfoBean;
    }
}
