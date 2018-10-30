package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;
import java.util.List;

public class PlanBean1 implements Serializable {

	private String time;
	private String shijian = "30";//持续时间
	private String plan;
	private String starttime;
	private String endtime;
	private int status = -1;//-1未完成， 1完成

	public PlanBean1() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getShijian() {
		return shijian;
	}

	public void setShijian(String shijian) {
		this.shijian = shijian;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
}
