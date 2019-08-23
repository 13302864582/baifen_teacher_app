package com.ucuxin.ucuxin.tec.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.format.Time;

import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExceptionManager implements UncaughtExceptionHandler{
	
	public static String TAG = "TAOJIE";
    
    private static final int MAX_LOG_MESSAGE_LENGTH = 200000;
    
    private static final long LOG_OUT_TIME = 1000 * 60 * 60 * 24 * 5;
    
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    
    
    private static final String LOG_DIR= SD_PATH + "/ucuxin/log/error/";
    
    private static final SimpleDateFormat FILENAME_FORMAT = 
        new SimpleDateFormat("yyyyMMdd-HHmmss",Locale.CHINESE);
    
    private static final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    
    private String verName;
    
    private int verCode;
    
    private String phone_model;
    
    private int phone_sdk;
    
    public ExceptionManager(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            verName = pi.versionName;
            verCode = pi.versionCode;
            phone_model = android.os.Build.MODEL;
            phone_sdk = android.os.Build.VERSION.SDK_INT;
            
            new Thread(){
                @Override
                public void run() {
                    deleteOutLogs();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteOutLogs(){
        File dir = new File(LOG_DIR);
        try {
            final long currTime = System.currentTimeMillis();
            File[] files = dir.listFiles(new FilenameFilter() {
                
                public boolean accept(File dir, String filename) {
                    File f = new File(dir.getAbsolutePath() + "/" + filename);
                    long time = f.lastModified();
                    return currTime - time > LOG_OUT_TIME;
                }
            });
            if(files == null){
                return;
            }
            for(File f : files){
                f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    public void uncaughtException(Thread t, Throwable e) {
        final Writer stackResult = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stackResult);
        e.printStackTrace(printWriter);

        try {
            File dir = new File(LOG_DIR);
            if (!dir.isDirectory() && !dir.exists()) {
                dir.mkdirs();
            }
            Time tmtxt = new android.text.format.Time();
            tmtxt.setToNow();
            String sTime = tmtxt.format("%Y-%m-%d %H:%M:%S");
            
            final File logFile = new File(
                    LOG_DIR + "/" 
                    + FILENAME_FORMAT.format(new Date(System.currentTimeMillis())) 
                    + ".txt");
            logFile.createNewFile();
            
            BufferedWriter bos = new BufferedWriter(new FileWriter(logFile));
            bos.write("\t\r\n==================LOG=================\t\r\n");
            bos.write("APP_VERSION:" + verName + "|" + verCode + "\t\r\n");
            bos.write("PHONE_MODEL:" + phone_model + "\t\r\n");
            bos.write("ANDROID_SDK:" + phone_sdk + "\t\r\n");
            bos.write(sTime + "\t\r\n");
            bos.write(stackResult.toString());
            bos.write("\t\r\n--------------------------------------\t\r\n");
            bos.flush();
            StringBuilder log = getLog();
            int keepOffset = Math.max(log.length() - MAX_LOG_MESSAGE_LENGTH, 0);
            if (keepOffset > 0) {
                log.delete(0, keepOffset);
            }
            bos.write(getLog().toString());
            bos.flush();
            bos.close();
            
//            ThreadPoolUtil.execute(new Runnable() {
//
//				@Override
//				public void run() {
//					String content = MyFileUtil.readFile(logFile.getAbsolutePath(), "utf-8").toString();
//		            TecApplication.mNetworkUtil.post(AppConfig.MAIL_URL, content);
//				}
//			});

            String content = MyFileUtil.readFile(logFile.getAbsolutePath(), "utf-8").toString();
            OkHttpHelper.postSingle(AppConfig.MAIL_URL,null,null,null,null);
        } catch (Exception ebos) {
            ebos.printStackTrace();
        }
        defaultHandler.uncaughtException(t, e); 
    }
    
    public StringBuilder getLog() {

        final StringBuilder log = new StringBuilder();
        try {
            ArrayList<String> commandLine = new ArrayList<String>();
            commandLine.add("logcat");//$NON-NLS-1$
            commandLine.add("-d");//$NON-NLS-1$

            Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[0]));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
        	LogUtils.e("TAG getLog failed", e.toString());//$NON-NLS-1$
        }
        return log;
    }
    
}