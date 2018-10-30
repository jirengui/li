package ruanjianbei.sport.mysport.bean;

/**
 * Created by li on 2018/4/13.
 */

public class ZhuCeBean {
    private String zhanghao;
    private String mima;
    private String yonghuming;
    private String diqu;
    private String xuexiao;
    private String xuehao;
    private String xingbie;
    public ZhuCeBean(String zhanghao, String mima, String yonghuming, String diqu, String xuexiao, String xuehao, String xingbie){
        this.zhanghao = zhanghao;
        this.mima = mima;
        this.yonghuming = yonghuming;
        this.diqu = diqu;
        this.xuexiao = xuexiao;
        this.xuehao = xuehao;
        this.xingbie = xingbie;

    }
    public ZhuCeBean(){}

    public String getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao;
    }

    public String getMima() {
        return mima;
    }

    public void setMima(String mima) {
        this.mima = mima;
    }

    public String getYonghuming() {
        return yonghuming;
    }

    public void setYonghuming(String yonghuming) {
        this.yonghuming = yonghuming;
    }

    public String getDiqu() {
        return diqu;
    }

    public void setDiqu(String diqu) {
        this.diqu = diqu;
    }

    public String getXuexiao() {
        return xuexiao;
    }

    public void setXuexiao(String xuexiao) {
        this.xuexiao = xuexiao;
    }

    public String getXuehao() {
        return xuehao;
    }

    public void setXuehao(String xuehao) {
        this.xuehao = xuehao;
    }

    public String getXingbie() {
        return xingbie;
    }

    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }
}
