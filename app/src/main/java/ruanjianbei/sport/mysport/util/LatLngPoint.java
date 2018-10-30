package ruanjianbei.sport.mysport.util;

import android.support.annotation.NonNull;

import com.amap.api.maps.model.LatLng;

/**
 * Created by li on 2018/5/10.
 */

public class LatLngPoint implements Comparable<LatLngPoint> {
    /**
     * 用于记录每一个点的序号
     */
    public int id;
    /**
     * 每一个点的经纬度
     */
    public LatLng latLng;

    public LatLngPoint(int id,LatLng latLng){
        this.id = id;
        this.latLng = latLng;
    }

    @Override
    public int compareTo(@NonNull LatLngPoint o) {
        if (this.id < o.id) {
            return -1;
        } else if (this.id > o.id)
            return 1;
        return 0;
    }
}
