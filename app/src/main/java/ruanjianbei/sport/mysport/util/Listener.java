package ruanjianbei.sport.mysport.util;

import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ruanjianbei.sport.mysport.bean.MessageEvent;

/**
 * Created by li on 2018/4/24.
 */

public class Listener extends WebSocketListener {
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        System.out.println("发送消息了 ");

    }



    @Override
    public void onMessage(WebSocket webSocket,final String text) {
        super.onMessage(webSocket, text);
        System.out.println("Socket接收到数据为：    "+text);
//        if (Xiaoxilei.otherId != -1) {
//            System.out.println("ID不为-1：    ");
//            String a[] = text.split("pIN1j0fd");
//            Map<String, String> map = new HashMap<>();
//            map.put(a[0], a[1]);
//            Xiaoxilei.xiaoxi.add(map);
//        }else {
//            System.out.println("ID为-1：    ");
            EventBus.getDefault().post(new MessageEvent(text));
//        }

    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        System.out.println("好友列表啊哦失败了：    ");
    }



}
