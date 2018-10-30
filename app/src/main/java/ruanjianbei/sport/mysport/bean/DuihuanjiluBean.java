package ruanjianbei.sport.mysport.bean;

public class DuihuanjiluBean {

	private Integer id;//记录Id
	private Integer uid;//用户id
	private Integer lid;//礼物id
	private Integer count;//数量
	private String time;//时间
	private LiWuBean liWuBean;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getLid() {
		return lid;
	}
	public void setLid(Integer lid) {
		this.lid = lid;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public LiWuBean getLiWuBean() {
		return liWuBean;
	}
	public void setLiWuBean(LiWuBean liWuBean) {
		this.liWuBean = liWuBean;
	}
	
	
}
