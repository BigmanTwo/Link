package com.example.administrator.link.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CloseUtil  {
    public CloseUtil(Closeable close){
        if(close!=null){
            try {
                close.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
