package com.example.virusapp.data;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
public class DataManager extends Service{
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        doMyJob(intent);
        return super.onStartCommand(intent,flags,startId);
    }
    private void doMyJob(Intent intent){
        new Thread(){
            @Override
            public void run(){
                //init database
                //maintain and update
                //respond to request
                //maintain a sequence of viewed news
            }
        }.start();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
