package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by li on 2018/5/25.
 */

public class RepinlunBean implements Serializable {

    private UserIndividualInfoBean huifuBean;
    private List<String> context;
    public UserIndividualInfoBean getHuifuBean() {
        return huifuBean;
    }

    public RepinlunBean(UserIndividualInfoBean hBean,List<String> list) {
        this.huifuBean = hBean;
        this.context = list;
    }

    public void setHuifuBean(UserIndividualInfoBean huifuBean) {
        this.huifuBean = huifuBean;
    }
    public List<String> getContext() {
        return context;
    }
    public void setContext(List<String> context) {
        this.context = context;
    }


}