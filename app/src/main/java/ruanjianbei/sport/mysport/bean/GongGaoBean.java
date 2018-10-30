package ruanjianbei.sport.mysport.bean;

public class GongGaoBean {

	private int id;
	
	private String name;
	private String context;
	private String tupian;
	private String time;
	
	public GongGaoBean(){}

	public GongGaoBean(int id,String name,String context, String tupian,String time) {
		this.id = id;
		this.name = name;
		this.context = context;
		this.tupian = tupian;
		this.time = time;
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
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getTupian() {
		return tupian;
	}
	public void setTupian(String tupian) {
		this.tupian = tupian;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
