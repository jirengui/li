package ruanjianbei.sport.mysport.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;

import com.alibaba.idst.nls.internal.utils.L;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ruanjianbei.sport.mysport.bean.JuBanSaiShiBean;

/**
 * Created by Kochiyasanae on 2018/4/8/008.
 */

public class HttpUtils {
    /**
     * 向服务器发送post请求，包含一些Map参数
     * @param address
     * @param callback
     * @param map
     */
    public static void post(String address, okhttp3.Callback callback, Map<String, String> map)
    {
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        if (map!=null)
        {
            //添加POST中传送过去的一些键值对信息
            for (Map.Entry<String, String> entry:map.entrySet())
            {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void postshuzu(String address, okhttp3.Callback callback, Map<String, String[]> map)
    {
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        if (map!=null)
        {
            //添加POST中传送过去的一些键值对信息
            for (Map.Entry<String, String[]> entry:map.entrySet())
            {
                builder.add(entry.getKey(), entry.getValue()[0]);
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 向服务器发送json数据
     * @param address 地址
     * @param callback 回调
     * @param jsonStr json数据
     */
    public static void postJson(String address, okhttp3.Callback callback, String jsonStr)
    {
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void upload(String userid, File file, Callback callback) {

        Bitmap bitmaps = getSmallBitmap(file.getPath());
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",  Bitmap2StrByBase64(bitmaps))
                .addFormDataPart("imagetype", "image/jpeg")
                .addFormDataPart("userId", userid)
                .build();
        Request request = new Request.Builder()
                .url(MyApplication.postfileUrl)
                .post(requestBody)
                .build();
            client.newCall(request).enqueue(callback);
//        execute(client, request);
        System.out.println("数据： " + request.toString());
        System.out.println("进来了：" + file.getPath());
    }
    public static void upzhanduitouxiang(String userid,String teamName, File file, Callback callback) {

        Bitmap bitmaps = getSmallBitmap(file.getPath());
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",  Bitmap2StrByBase64(bitmaps))
                .addFormDataPart("imagetype", "image/jpeg")
                .addFormDataPart("id", userid)
                .addFormDataPart("teamName", teamName)
                .build();
        Request request = new Request.Builder()
                .url(MyApplication.createTeam)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
//        execute(client, request);
        System.out.println("数据： " + request.toString());
        System.out.println("进来了：" + file.getPath());
    }
    public static void huoqutouxiang(String uri,  Callback callback){
        String mBaseUrl = MyApplication.imageUri + uri;
        System.out.println("获取头像路径：" + mBaseUrl);
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        Request request = new Request.Builder()
                .url(mBaseUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void fabiaoshuoshuo(String userid,Map<String, String[]> map, List<String> file, Callback callback) {
        List<Bitmap> list = new ArrayList<>();

        if (file != null) {
            for (int i = 0; i < file.size(); i++) {
                list.add(getSmallBitmap(file.get(i)));
            }
            System.out.println("图片张数： " + file.size());
        }

        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

            for (int i = 0; i < list.size(); i++) {
                multipartBodyBuilder.addFormDataPart("file", Bitmap2StrByBase64(list.get(i)));
            }
        multipartBodyBuilder.addFormDataPart("imagetype", "image/jpeg");
        multipartBodyBuilder.addFormDataPart("userId", userid);
        if (map!=null)
        {
            //添加POST中传送过去的一些键值对信息
            for (Map.Entry<String, String[]> entry:map.entrySet())
            {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue()[0]);
            }
        }
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(MyApplication.addaddshuoshuoUrl)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
//        execute(client, request);
        System.out.println("数据： " + request.toString());
    }
    public static void jieshupaobu(String userid, Map<String, String[]> map, List<String> file, Callback callback) {
        List<Bitmap> list = new ArrayList<>();

        if (file != null) {
            for (int i = 0; i < file.size(); i++) {
                list.add(getSmallBitmap(file.get(i)));
            }
            System.out.println("图片张数： " + file.size());
        }

        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        for (int i = 0; i < list.size(); i++) {
            multipartBodyBuilder.addFormDataPart("file", Bitmap2StrByBase64(list.get(i)));
        }
        multipartBodyBuilder.addFormDataPart("imagetype", "image/jpeg");
        multipartBodyBuilder.addFormDataPart("uid", userid);
        if (map!=null)
        {
            //添加POST中传送过去的一些键值对信息
            for (Map.Entry<String, String[]> entry:map.entrySet())
            {
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue()[0]);
            }
        }
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(MyApplication.updatelichengurl)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
//        execute(client, request);
        System.out.println("数据： " + request.toString());
    }

    public static void chuangjiansaishi(JuBanSaiShiBean juBanSaiShiBean, Callback callback) {
        List<Bitmap> list = new ArrayList<>();

        if (juBanSaiShiBean.getTupian() != null) {
                list.add(getSmallBitmap(juBanSaiShiBean.getTupian()));
        }

        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        juBanSaiShiBean.setTupian(Bitmap2StrByBase64(list.get(0)));
        Gson gson = new Gson();
        String juban = gson.toJson(juBanSaiShiBean, JuBanSaiShiBean.class);
        multipartBodyBuilder.addFormDataPart("imagetype", "image/jpeg");
        multipartBodyBuilder.addFormDataPart("uid", String.valueOf(juBanSaiShiBean.getUid()));
        multipartBodyBuilder.addFormDataPart("juban", juban);

        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(MyApplication.insertSaiShi)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
//        execute(client, request);
        System.out.println("数据： " + request.toString());
    }

    public static void haoyouliebiao(String userId,  Callback callback){
        OkHttpClient client  = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        Request request = new Request.Builder()
                .url(MyApplication.postfileUrl)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }
}

