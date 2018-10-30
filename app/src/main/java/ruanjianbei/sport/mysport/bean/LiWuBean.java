package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;

public class LiWuBean implements Serializable {

	private int id;
	private String name;
	private String jifen;
	private String shuxing;
	private String tupian;
	private String type;
	private int kucun;
	
	public LiWuBean(int id,String name,String jifen,String shuxing,String tupian,String type,int kucun) {
		this.id= id;
		this.name = name;
		this.jifen = jifen;
		this.shuxing = shuxing;
		this.tupian = tupian;
		this.type = type;
		this.kucun = kucun;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJifen() {
		return jifen;
	}
	public void setJifen(String jifen) {
		this.jifen = jifen;
	}
	public String getShuxing() {
		return shuxing;
	}
	public void setShuxing(String shuxing) {
		this.shuxing = shuxing;
	}
	public String getTupian() {
		return tupian;
	}
	public void setTupian(String tupian) {
		this.tupian = tupian;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getKucun() {
		return kucun;
	}
	public void setKucun(int kucun) {
		this.kucun = kucun;
	}
	
}
