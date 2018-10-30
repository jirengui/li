package ruanjianbei.sport.mysport.bean;

public class PaiMingBean {

	private String name;
	private double licheng;
	private String touxiang;
	
	
	public PaiMingBean(String name,Double licheng,String touxiang) {
		this.name = name;
		this.licheng = licheng;
		this.touxiang = touxiang;
	}
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLicheng() {
		return licheng;
	}
	public void setLicheng(double licheng) {
		this.licheng = licheng;
	}



	public String getTouxiang() {
		return touxiang;
	}



	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	
	
}
