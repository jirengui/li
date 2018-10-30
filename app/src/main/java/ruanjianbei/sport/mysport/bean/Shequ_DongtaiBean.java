package ruanjianbei.sport.mysport.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2018/5/16.
 */

public class Shequ_DongtaiBean implements Serializable {
    private int id;//动态文章ID
    private String dongtai_touxiang;
    private String dongtai_name;
    private String dongtai_shijian;
    private String dongtai_neirong;
    private String dongtai_pinlun;
    private String dongtai_fenxiang;
    private String dongtai_dianzan;
    private List<String> dongtai_tupian = new ArrayList<>();
    private int dianzanstatus;
    private int fenxiangstatus;
    private int pinlunstatus;
    private Boolean guanzhu;
    public Shequ_DongtaiBean(){

    }
    public Shequ_DongtaiBean(int id,String dongtai_touxiang, String dongtai_name
            , String dongtai_shijian, String dongtai_neirong, String dongtai_pinlun
            , String dongtai_fenxiang, String dongtai_dianzan,List<String> dongtai_tupian
            ,int dianzanstatus,int fenxaingstatus,int pinlunstatus){
        this.dongtai_touxiang = dongtai_touxiang;
        this.id = id;
        this.dianzanstatus = dianzanstatus;
        this.fenxiangstatus = fenxaingstatus;
        this.pinlunstatus = pinlunstatus;
        this.dongtai_name = dongtai_name;
        this.dongtai_shijian = dongtai_shijian;
        this.dongtai_neirong = dongtai_neirong;
        this.dongtai_pinlun = dongtai_pinlun;
        this.dongtai_fenxiang = dongtai_fenxiang;
        this.dongtai_dianzan = dongtai_dianzan;
        this.dongtai_tupian = dongtai_tupian;
    }
    public Shequ_DongtaiBean(int id,String dongtai_touxiang, String dongtai_name, String dongtai_shijian, String dongtai_neirong, String dongtai_pinlun, String dongtai_fenxiang, String dongtai_dianzan,List<String> dongtai_tupian  ){
        this.dongtai_touxiang = dongtai_touxiang;
        this.dongtai_name = dongtai_name;
        this.dongtai_shijian = dongtai_shijian;
        this.dongtai_neirong = dongtai_neirong;
        this.dongtai_pinlun = dongtai_pinlun;
        this.dongtai_fenxiang = dongtai_fenxiang;
        this.dongtai_dianzan = dongtai_dianzan;
        this.dongtai_tupian = dongtai_tupian;
        this.id = id;
    }
    public Shequ_DongtaiBean(String dongtai_touxiang, String dongtai_name, String dongtai_shijian, String dongtai_neirong, String dongtai_pinlun, String dongtai_fenxiang, String dongtai_dianzan ){
        this.dongtai_touxiang = dongtai_touxiang;
        this.dongtai_name = dongtai_name;
        this.dongtai_shijian = dongtai_shijian;
        this.dongtai_neirong = dongtai_neirong;
        this.dongtai_pinlun = dongtai_pinlun;
        this.dongtai_fenxiang = dongtai_fenxiang;
        this.dongtai_dianzan = dongtai_dianzan;
    }
    public String getDongtai_touxiang() {
        return dongtai_touxiang;
    }

    public void setDongtai_touxiang(String dongtai_touxiang) {
        this.dongtai_touxiang = dongtai_touxiang;
    }

    public String getDongtai_name() {
        return dongtai_name;
    }

    public void setDongtai_name(String dongtai_name) {
        this.dongtai_name = dongtai_name;
    }

    public String getDongtai_shijian() {
        return dongtai_shijian;
    }

    public void setDongtai_shijian(String dongtai_shijian) {
        this.dongtai_shijian = dongtai_shijian;
    }

    public String getDongtai_neirong() {
        return dongtai_neirong;
    }

    public void setDongtai_neirong(String dongtai_neirong) {
        this.dongtai_neirong = dongtai_neirong;
    }

    public String getDongtai_pinlun() {
        return dongtai_pinlun;
    }

    public void setDongtai_pinlun(String dongtai_pinlun) {
        this.dongtai_pinlun = dongtai_pinlun;
    }

    public String getDongtai_fenxiang() {
        return dongtai_fenxiang;
    }

    public void setDongtai_fenxiang(String dongtai_fenxiang) {
        this.dongtai_fenxiang = dongtai_fenxiang;
    }

    public String getDongtai_dianzan() {
        return dongtai_dianzan;
    }

    public void setDongtai_dianzan(String dongtai_dianzan) {
        this.dongtai_dianzan = dongtai_dianzan;
    }

    public List<String> getDongtai_tupian() {
        return dongtai_tupian;
    }

    public void setDongtai_tupian(List<String> dongtai_tupian) {
        this.dongtai_tupian = dongtai_tupian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDianzanstatus() {
        return dianzanstatus;
    }

    public void setDianzanstatus(int dianzanstatus) {
        this.dianzanstatus = dianzanstatus;
    }

    public int getFenxiangstatus() {
        return fenxiangstatus;
    }

    public void setFenxiangstatus(int fenxiangstatus) {
        this.fenxiangstatus = fenxiangstatus;
    }

    public int getPinlunstatus() {
        return pinlunstatus;
    }

    public void setPinlunstatus(int pinlunstatus) {
        this.pinlunstatus = pinlunstatus;
    }

    public Boolean getGuanzhu() {
        return guanzhu;
    }

    public void setGuanzhu(Boolean guanzhu) {
        this.guanzhu = guanzhu;
    }
}
