package ruanjianbei.sport.mysport.bean;

/**
 * Created by li on 2018/4/25.
 */

public class MessageEvent {
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
