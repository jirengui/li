package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;

public class TeamInfoBean implements Serializable {
	
	private int id;	
	private String rank;       //等级
	private String point;      //经验值
	private int membernumber;  //成员数字
	private String station;    //驻地
	private int paiming;       
	private String xuanyan;    //宣言
	private String teamname;   //战队名
	private String creater;    //创始人
	private String creatertouxiang;  //创始人头像
	private String viceheader;       //副队长
	private String viceheadertouxiang;//副队长头像
	private String createtime;       //创建时间
	private String touxiang;
	
	
	private TeamInfoBean() {
		super();
	}
	
	
	public  TeamInfoBean(int id,String rank,String point,int membernumber
			,String station,int paiming,String xuanyan,String teamname,String creater,String createtime,String touxiang) {
		this.id = id;
		this.rank = rank;
		this.point = point;
		this.membernumber = membernumber;
		this.station = station;
		this.paiming = paiming;
		this.xuanyan = xuanyan;
		this.teamname = teamname;
		this.creater = creater;
		this.createtime= createtime;
		this.touxiang = touxiang;
		
	}
	
	public  TeamInfoBean(int id,String rank,String point,int membernumber
			,String station,int paiming,String xuanyan,String teamname,String creater,String createtime,String touxiang,String creatertouxiang,String viceheader,String viceheadertouxiang) {
		this.id = id;
		this.rank = rank;
		this.point = point;
		this.membernumber = membernumber;
		this.station = station;
		this.paiming = paiming;
		this.xuanyan = xuanyan;
		this.teamname = teamname;
		this.creater = creater;
		this.createtime= createtime;
		this.touxiang = touxiang;
		this.creatertouxiang = creatertouxiang;
		this.viceheader = viceheader;
		this.viceheadertouxiang = viceheadertouxiang;
		
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public int getMembernumber() {
		return membernumber;
	}
	public void setMembernumber(int membernumber) {
		this.membernumber = membernumber;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public int getPaiming() {
		return paiming;
	}
	public void setPaiming(int paiming) {
		this.paiming = paiming;
	}
	public String getXuanyan() {
		return xuanyan;
	}
	public void setXuanyan(String xuanyan) {
		this.xuanyan = xuanyan;
	}
	
	public String getTeamname() {
		return teamname;
	}
	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}


	public String getCreatertouxiang() {
		return creatertouxiang;
	}


	public void setCreatertouxiang(String creatertouxiang) {
		this.creatertouxiang = creatertouxiang;
	}


	public String getViceheader() {
		return viceheader;
	}


	public void setViceheader(String viceheader) {
		this.viceheader = viceheader;
	}


	public String getViceheadertouxiang() {
		return viceheadertouxiang;
	}


	public void setViceheadertouxiang(String viceheadertouxiang) {
		this.viceheadertouxiang = viceheadertouxiang;
	}

}
