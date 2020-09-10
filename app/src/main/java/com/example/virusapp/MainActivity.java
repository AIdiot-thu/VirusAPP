package com.example.virusapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.contentcapture.DataRemovalRequest;

import com.example.virusapp.data.*;
import com.example.virusapp.data.DataManager;
import com.example.virusapp.data.Parser;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/22573301/how-to-pass-a-handler-from-activity-to-service
    private ServiceConnection mConnection;
    private Handler dataHandler;
    private DataManager mService=null;
    private Boolean misBound=false;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_news, R.id.navigation_map, R.id.navigation_info, R.id.navigation_scholar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //service
        dataHandler = new  Handler(){//handler must be written in the main thread
            // 通过复写handlerMessage()从而确定更新UI的操作
            @Override
            public void handleMessage(Message msg) {
                System.out.println("handling message");
                ArrayList<NewsData> newslist=(ArrayList<NewsData>)msg.obj;
                System.out.println(newslist.get(0));
            }
        };
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder)
            {
                //this function is not guaranteed to be executed after bind
                //see:https://blog.csdn.net/u013427822/article/details/72771081
                //see:https://blog.csdn.net/qq_26918031/article/details/59055416?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param&depth
                //_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-2.channel_param
                mService = (DataManager)((DataManager.LocalBinder)iBinder).getInstance();
                mService.setHandler(dataHandler);
                misBound = true;
                System.out.println("misbound");
                MainActivity.this.afterBind();

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName)
            {
                mService = null;
            }
        };
        Intent intent = new Intent(this, DataManager.class);
        intent.putExtra("action",0);
        startService(intent);
        bindService(new Intent(this,
                DataManager.class), mConnection, Context.BIND_AUTO_CREATE);
        //
    }
    protected void afterBind(){
        //something after binding service
    }

    public DataManager getmService() {
        return mService;
    }

    public Boolean getMisBound() {
        return misBound;
    }
}