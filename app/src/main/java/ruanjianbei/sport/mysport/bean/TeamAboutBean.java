package ruanjianbei.sport.mysport.bean;

import java.util.List;

public class TeamAboutBean {

	private List<UserIndividualInfoBean> list;
	private List<GongGaoBean> list1;
	private TeamInfoBean teamInfoBean;
	
	public TeamAboutBean() {
		super();
	}
	
	
	public List<UserIndividualInfoBean> getList() {
		return list;
	}
	public void setList(List<UserIndividualInfoBean> list) {
		this.list = list;
	}
	public List<GongGaoBean> getList1() {
		return list1;
	}
	public void setList1(List<GongGaoBean> list1) {
		this.list1 = list1;
	}
	public TeamInfoBean getTeamInfoBean() {
		return teamInfoBean;
	}
	public void setTeamInfoBean(TeamInfoBean teamInfoBean) {
		this.teamInfoBean = teamInfoBean;
	}
	
}
