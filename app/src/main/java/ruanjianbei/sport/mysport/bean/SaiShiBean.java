package ruanjianbei.sport.mysport.bean;


import java.io.Serializable;

public class SaiShiBean implements Serializable{

   private int id;//赛事id
   
   private UserIndividualInfoBean userIndividualInfoBean;//发起人的信息
   
   private String context;//发起的内容
   
   private String time;//发起的时间
   
   private String kaishitime;//开始时间
   
   private String jieshutime;//结束时间
   
   private int count;//参与人数
   
   private String  jiangli;//奖励
   
   private int status;

   private String tupian;

	private int perStatus;//个人赛事状态
   
   public SaiShiBean(int id,UserIndividualInfoBean userIndividualInfoBean,String context
		   ,int count,String kaishitime,String jieshushijian,String time,String jiangli,int status) {
	   this.id = id;
	   this.userIndividualInfoBean = userIndividualInfoBean;
	   this.context = context;
	   this.time = time;
	   this.kaishitime = kaishitime;
	   this.jieshutime = jieshushijian;
	   this.count = count;
	   this.jiangli = jiangli; 
	   this.status = status;
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

public String getContext() {
	return context;
}

public void setContext(String context) {
	this.context = context;
}

public String getTime() {
	return time;
}

public void setTime(String time) {
	this.time = time;
}

public String getKaishitime() {
	return kaishitime;
}

public void setKaishitime(String kaishitime) {
	this.kaishitime = kaishitime;
}

public String getJieshushijian() {
	return jieshutime;
}

public void setJieshushijian(String jieshushijian) {
	this.jieshutime = jieshushijian;
}

public int getCount() {
	return count;
}

public void setCount(int count) {
	this.count = count;
}

public String getJiangli() {
	return jiangli;
}

public void setJiangli(String jiangli) {
	this.jiangli = jiangli;
}

public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}


	public String getTupian() {
		return tupian;
	}

	public void setTupian(String tupian) {
		this.tupian = tupian;
	}

	public int getPerStatus() {
		return perStatus;
	}

	public void setPerStatus(int perStatus) {
		this.perStatus = perStatus;
	}
}
