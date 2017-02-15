package com.example.administrator.link.utils;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/13.
 */
public class FileUtil {
    public static boolean deleteFile(Context context,String filename){
        return context.deleteFile(filename);
    }
    public  static boolean exists(Context context,String filename){
        return   new File(context.getFilesDir(),filename).exists();
    }
    public static boolean writeFile(Context context,String name ,String content){
        boolean success=false;
        FileOutputStream outputStream=null;
        try {
            outputStream=context.openFileOutput(name,Context.MODE_PRIVATE);
            byte[] bytes=content.getBytes();
            outputStream.write(bytes);
            success=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            new CloseUtil(outputStream);
        }
        return success;
    }
    public static  boolean writeFile(String fileName,String context){
        boolean success=false;
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(fileName);
            byte[] bytes=context.getBytes();
            outputStream.write(bytes);
            success=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new CloseUtil(outputStream);
        }
        return success;
    }
    public static  String readFile(Context context,String fileName){
        if(!exists(context,fileName)){
            return null;
        }
        FileInputStream inputStream=null;
        String content=null;
        try {
            inputStream=context.openFileInput(fileName);
            if (inputStream!=null){
                byte[] bytes=new byte[1024];
                ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
                while (true){
                    int readLength=inputStream.read(bytes);
                    if(readLength==-1){
                        break;
                    }
                    arrayOutputStream.write(bytes,0,readLength);
                }
                inputStream.close();
                arrayOutputStream.close();
                content=new String(arrayOutputStream.toByteArray());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content=null;
        }
        finally {
            new CloseUtil(inputStream);
        }
        return content;
    }
    public static String readFile(String fileName){
        if (fileName==null ||  !new File(fileName).exists()){
            return null;
        }
        FileInputStream inputStream=null;
        String content=null;
        try {
            inputStream=new FileInputStream(fileName);
            if (inputStream!=null){
                byte[] buffer=new byte[1024];
                ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
                while (true){
                    int readLength=inputStream.read(buffer);
                    if (readLength==-1){
                        break;
                    }
                    arrayOutputStream.write(buffer,0,readLength);
                }
                inputStream.close();
                arrayOutputStream.close();
                content=new String(arrayOutputStream.toByteArray());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new CloseUtil(inputStream);
        }
        return content;
    }
    public static String readAssets(Context context,String filaName){
        InputStream is=null;
        String content=null;
        try {
            is=context.getAssets().open(filaName);
            if (is!=null){
                byte[] buffer=new byte[1024];
                ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
                while (true){
                    int readLength=is.read(buffer);
                    if(readLength==-1){
                        break;
                    }
                    arrayOutputStream.write(buffer,0,readLength);

                }
                is.close();
                arrayOutputStream.close();
                content=new String(arrayOutputStream.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new CloseUtil(is);
        }
        return content;
    }
}
