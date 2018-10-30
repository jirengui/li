package ruanjianbei.sport.mysport.bean;



import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2018/5/29.
 */

public class PaoBuJieGuoBean {
    private List<LatLng> guiji = new ArrayList<LatLng>();
    private int userId;
    private double licheng;
    private String time;
    private String peisu;
    private int paobumoshi;
    private String jietu;
    private ArrayList<String> paizhaodidian = new ArrayList<String>();


    public PaoBuJieGuoBean(List<LatLng> guiji, int userId, double licheng,ArrayList<String> paizhaodidian){
        this.guiji = guiji;
        this.userId = userId;
        this.licheng = licheng;
        this.paizhaodidian = paizhaodidian;
    }
    
    public PaoBuJieGuoBean(List<LatLng> guiji, String time,Double licheng){
        this.guiji = guiji;
        this.time = time;
        this.licheng = licheng;
    }

    
    public PaoBuJieGuoBean(List<LatLng> guiji, String time,Double licheng,String peisu,int paobumoshi){
        this.guiji = guiji;
        this.time = time;
        this.licheng = licheng;
        this.peisu = peisu;
        this.paobumoshi = paobumoshi;
    }

    public List<LatLng> getGuiji() {
        return guiji;
    }

    public void setGuiji(List<LatLng> guiji) {
        this.guiji = guiji;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLicheng() {
        return licheng;
    }

    public void setLicheng(double licheng) {
        this.licheng = licheng;
    }

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPeisu() {
		return peisu;
	}

	public void setPeisu(String peisu) {
		this.peisu = peisu;
	}

	public int getPaobumoshi() {
		return paobumoshi;
	}

	public void setPaobumoshi(int paobumoshi) {
		this.paobumoshi = paobumoshi;
	}

    public String getJietu() {
        return jietu;
    }

    public void setJietu(String jietu) {
        this.jietu = jietu;
    }

    public ArrayList<String> getPaizhaodidian() {
        return paizhaodidian;
    }

    public void setPaizhaodidian(ArrayList<String> paizhaodidian) {
        this.paizhaodidian = paizhaodidian;
    }
}
