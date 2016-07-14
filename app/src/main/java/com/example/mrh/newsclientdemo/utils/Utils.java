package com.example.mrh.newsclientdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.SystemClock;

import com.example.mrh.newsclientdemo.service.DownloadService;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;


/**
 * Created by MR.H on 2016/7/7 0007.
 */
public class Utils {

    private static String sFilePath;


    //绑定服务
    public static boolean bindToService(Context context, ServiceConnection connection){
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
        return context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    //解绑服务
    public static void unBindToService(Context context, ServiceConnection connection){
        context.unbindService(connection);
    }
    //更换json中Newslist名称
    public static String resetListName(String json){
        String[] split = json.split("\"", 3);
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("\"listName\"");
        sb.append(split[2]);
        return sb.toString();

    }
    //更换json中VedioName名称
    public static String resetVedioName(String json){
        String[] split = json.split("\"", 3);
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("\"vedios\"");
        sb.append(split[2]);
        return sb.toString();

    }
    //更换json中News名称
    public static String resetNewsName(String json){
        String[] split = json.split("\"", 3);
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("\"news\"");
        sb.append(split[2]);
        return sb.toString();

    }
    //解析json
    public static Object Parse(String json, Class<?> cls){
        Gson gson =  new Gson();
        Object object = gson.fromJson(json, cls);
        return object;
    }
    //设置缓存, 可设置缓存时间，默认10分钟
    public static void setData(String json, String fileName, int minute){
        String sEncode = null;
        int keepTime;
        if (minute == -1){
            keepTime = 10;
        }else {
            keepTime = minute;
        }
        try{
            sEncode = MD5Encoder.encode(fileName);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (!sEncode.isEmpty()){
            File dir = new File(getMyCachePath());
            if (isNotExists(dir)){
                File file = new File(getMyCachePath() + File.separator + sEncode + ".txt");
                Writer sFw = null;
                long deadline = SystemClock.elapsedRealtime() + keepTime*60*1000;
                try{
                    sFw = new FileWriter(file);
                    sFw.write(deadline + "\n");
                    sFw.write(json);
                    sFw.flush();
                } catch (IOException e){
                    e.printStackTrace();
                }finally{
                    IOUtils.close(sFw);
                }
            }
        }
    }
    //设置缓存
    public static void setData(String json, String fileName){
        setData(json, fileName, -1);
    }
    //取出缓存
    public static String getData( String fileName){
        String sEncode = null;
        try{
            sEncode = MD5Encoder.encode(fileName);
        } catch (Exception e){
            e.printStackTrace();
        }
        File file = new File(getMyCachePath() + File.separator + sEncode + ".txt");
        if (file.exists()){
            String data;
            BufferedReader br = null;
            StringBuilder sb;
            long realtime = SystemClock.elapsedRealtime();
            try{
                br = new BufferedReader(new FileReader(file));
                sb = new StringBuilder();
                data = br.readLine();
                if (realtime < Long.parseLong(data)){
                    data = br.readLine();
                    while (data != null ){
                        sb.append(data);
                        data = br.readLine();
                    }
                    return sb.toString();
                }

            } catch (Exception e){
                e.printStackTrace();
            } finally{
                IOUtils.close(br);
            }
        }
        return null;
    }

    //判断文件夹是否存在
    private static boolean isNotExists (File dir) {
        if (!dir.exists() || dir.isFile()){
            return dir.mkdirs();
        }
        return true;
    }
    public static void setMyCachePath (String path){
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String path =  context.getCacheDir().getAbsolutePath();
        sFilePath = path + File.separator + "NewsClientDemo";
    }
    public static Picasso getMyPicasso(Context context){
        /**
         * 设置超时时间
         */
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);

        /**
         * 加载https的网址
         */
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(okHttpClient))
                .build();
        return picasso;
    }

    public static String getMyCachePath (){
        return sFilePath;
    }
    //像素转换
    public static int px2dip(Context context, float px){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
    public static int dip2px(Context context, float dip){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
    //float转换指定位数的小数
    public static String formatFloat(float num, String digit){
        DecimalFormat decimalFormat = new DecimalFormat(digit);
        return decimalFormat.format(num);
    }

    //测试用haha
    public static void setTestData(String json, String fileName){
        if (!fileName.isEmpty()){
            File dir = new File(getMyCachePath());
            if (isNotExists(dir)){
                File file = new File(getMyCachePath() + File.separator + fileName + ".txt");
                Writer sFw = null;
                long deadline = SystemClock.elapsedRealtime() + 10*60*1000;
                try{
                    sFw = new FileWriter(file);
                    sFw.write(deadline + "\n");
                    sFw.write(json);
                    sFw.flush();
                } catch (IOException e){
                    e.printStackTrace();
                }finally{
                    IOUtils.close(sFw);
                }
            }
        }
    }
}
