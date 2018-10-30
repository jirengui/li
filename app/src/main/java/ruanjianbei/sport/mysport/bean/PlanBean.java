package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;
import java.util.List;

public class PlanBean implements Serializable {

	private String time;
	private List<String> plan;
	private List<String> sporttime;
	private List<String> starttime;
	private List<Integer> status;
	
	public PlanBean() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	


	public List<String> getPlan() {
		return plan;
	}

	public void setPlan(List<String> plan) {
		this.plan = plan;
	}


	public List<String> getSporttime() {
		return sporttime;
	}

	public void setSporttime(List<String> sporttime) {
		this.sporttime = sporttime;
	}

	public List<String> getStarttime() {
		return starttime;
	}

	public void setStarttime(List<String> starttime) {
		this.starttime = starttime;
	}

	public List<Integer> getStatus() {
		return status;
	}

	public void setStatus(List<Integer> status) {
		this.status = status;
	}
}
