package ruanjianbei.sport.mysport.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by li on 2018/7/18.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String Create_contact = "CREATE TABLE tb_information (" +
            "userid INTEGER," +
            "username text DEFAULT NULL," +
            "password text DEFAULT NULL," +
            "name text DEFAULT NULL," +
            "school text DEFAULT NULL," +
            "jifen text DEFAULT NULL," +
            "licheng text DEFAULT NULL," +
            "xueqilicheng text DEFAULT NULL," +
            "day text DEFAULT NULL," +
            "sex text DEFAULT NULL," +
            "touxiang text DEFAULT NULL," +
            "xuehao text DEFAULT NULL," +
            "diqu text DEFAULT NULL," +
            "time text DEFAULT NULL," +
            "stautus text DEFAULT NULL," +
            "zongpaiming text DEFAULT NULL," +
            "shebei text DEFAULT NULL," +
            "tname text DEFAULT NULL," +
            "vname text DEFAULT NULL," +
            "quanxian INTEGER ," +
            "zonglicheng text DEFAULT NULL ," +
            "age INTEGER ," +
            "fid INTEGER ,"+
            "gid INTEGER ,"+
            "dengji text DEFAULT NULL," +
            "zhandui text,"+
            "zhanduizhiwu text"+
            ")";
//    public static final String Create_contact = "create table person(_id integer primary key autoincrement, " +
//            "name char(10), " +
//            "name char(10), " +
//            "salary char(20), " +
//            "phone integer(20))";


    public MySQLiteOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //当创建数据库的时候会调用此方法在此方法中创建表
        db.execSQL(Create_contact);
        System.out.println("数据库已建立");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}