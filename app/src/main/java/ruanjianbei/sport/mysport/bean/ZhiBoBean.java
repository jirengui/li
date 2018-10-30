package ruanjianbei.sport.mysport.bean;

public class ZhiBoBean {

	private int id;
	private UserIndividualInfoBean userIndividualInfoBean;
	private int count;
	private String biaoti;
	private String type;
	private String tupian;
	
	
	public ZhiBoBean(int id,UserIndividualInfoBean userIndividualInfoBean,int count,String biaoti,String type,String tupian) {
		this.id=id;
		this.userIndividualInfoBean = userIndividualInfoBean;
		this.count = count;
		this.biaoti = biaoti;
		this.type = type;
		this.tupian = tupian;
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserIndividualInfoBean getUserIndividualInfoBean() {
		return userIndividualInfoBean;
	}
	public void setUserIndividualInfoBean(UserIndividualInfoBean userIndividualInfoBean) {
		this.userIndividualInfoBean = userIndividualInfoBean;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getBiaoti() {
		return biaoti;
	}
	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTupian() {
		return tupian;
	}
	public void setTupian(String tupian) {
		this.tupian = tupian;
	}
	
	
	
}
