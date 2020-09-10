package com.example.virusapp.data;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
class Table{
    static public HashMap<String,Integer> string2Code;
    static public void initTable(){
        string2Code.put("Hong Kong",11);
        string2Code.put("Xinjiang",30);
        string2Code.put("Beijing",1);
        string2Code.put("Sichuan",24);
        string2Code.put("Gansu",5);
        string2Code.put("Shanghai",26);
        string2Code.put("Guangdong",4);
        string2Code.put("Taiwan",29);
        string2Code.put("Hebei",9);

        string2Code.put("Shannxi",23);
        string2Code.put("Shanxi",27);
        string2Code.put("Yunnan",32);
        string2Code.put("Chongqing",2);
        string2Code.put("Inner Mongol",20);
        string2Code.put("Shandong",25);
        string2Code.put("Zhejiang",33);
        string2Code.put("Tianjin",28);
        string2Code.put("Liaoning",18);
        string2Code.put("Fujian",3);
        string2Code.put("Jiangsu",16);
        string2Code.put("Hainan",8);
        string2Code.put("Macao",19);
        string2Code.put("Jilin",15);
        string2Code.put("Hubei",14);
        string2Code.put("Jiangxi",17);
        string2Code.put("Heilongjiang",12);
        string2Code.put("Anhui",0);
        string2Code.put("Guizhou",7);
        string2Code.put("Hunan",13);
        string2Code.put("Henan",10);
        string2Code.put("Guangxi",6);
        string2Code.put("Ningxia",21);
        string2Code.put("Qinghai",22);
        string2Code.put("Xizang",31);



    }
}

public class DataManager extends Service{
    Parser parser;
    ArrayList<LocalData> localDataList;
    ArrayList<NewsSumData> newsSumList;
    //add
    ArrayList<NewsData> newsReadList;
    ScheduledExecutorService scheduledExecutorService;

    private Handler mhandler=null;
    private final IBinder mIBinder = new LocalBinder();
    private static final String TAG = "DataManager";
    public boolean init=false;

    private ArrayList<Entity> tmp_entities;
    @Override
    public void onCreate() {
        parser=new Parser();
        localDataList=new ArrayList<LocalData>();
        newsSumList=new ArrayList<NewsSumData>();
        newsReadList=new ArrayList<NewsData>();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        Bundle bundle = intent.getExtras();
        if(bundle.getInt("action")==0) {
            Init(intent,false);
            System.out.println("news num:"+newsSumList.size());
        }
        //refresh every five minute
        scheduledExecutorService= Executors.newScheduledThreadPool(3);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run(){
                refreshNews();
            }
        },0,2, TimeUnit.MINUTES);

        return super.onStartCommand(intent,flags,startId);
    }
    public void Init(Intent intent,boolean join){
        Thread t=new Thread(){
            @Override
            public void run(){
                //init database
                synchronized (parser) {
                    synchronized (localDataList) {
                        localDataList = parser.getVirusData();
                        //localDataList.notify();
                    }
                    synchronized (newsSumList) {
                        newsSumList = parser.getVirusNewsList();
                        //newsSumList.notify();
                    }
                    System.out.println("virus data initialized");
                    parser.notifyAll();
                }
                init=true;
                //maintain and update
                //respond to request
                //maintain a sequence of viewed news
            }
        };
        t.start();
        if(join) {
            try {
                t.join(10000);
            } catch (Exception e) {
            }
        }
    }
    public void refreshNews(){
        if(init==false)return;
        Thread t=new Thread(){
            @Override
            public void run(){
                //init database
                synchronized (parser) {
                    synchronized (newsSumList) {
                        ArrayList<NewsSumData> lastestNews=parser.getLatestNewsList();
                        String endId=newsSumList.get(newsSumList.size()-1).id;
                        int index=0;
                        for(int i=lastestNews.size()-1;i>=0;i--){
                            if(lastestNews.get(i).id==endId){
                                index=i;
                                break;
                            }
                        }
                        for(int i=index;i<lastestNews.size();i++){
                            newsSumList.add(lastestNews.get(i));
                        }
                        //newsSumList.notify();
                    }
                    System.out.println("virus data refreshed");
                    parser.notifyAll();
                }
            }
        };
        t.start();
        /*
        {
            try {
                t.join(10000);
            } catch (Exception e) {
            }
        }*/
    }

    //should run in a sub thread
    private NewsData GetNewsData(final String id){
        NewsData news;
        synchronized (parser)
        {
            news =parser.getVirusNews(id);
        }
        return news;
    }
    public NewsData getNewsByIndex(final int index){
        final NewsData[] singleData = new NewsData[1];
        final String id=newsSumList.get(index).id;
        Thread thread = new Thread() {
            @Override
            public void run() {
                singleData[0] =(GetNewsData(id));
            }
        };
        thread.start();
        try {
            thread.join();
        }
        catch (Exception e){
        }
        return singleData[0];
    }
    public ArrayList<Entity> getEntities(final String key){
        tmp_entities=new ArrayList<Entity>();
        Thread t=new Thread(){
            @Override
            public void run(){
                //init database
                synchronized (parser) {
                    {
                        tmp_entities =parser.getVirusEntity(key);
                    }
                    System.out.println("entity parsed");
                    parser.notifyAll();
                }
            }
        };
        t.start();
        try {
            t.join();
        }catch (Exception e){
            System.out.println(e);
        }
        return tmp_entities;
    }
    //add
    public ArrayList<NewsData> searchNews(final String keyword){
        final ArrayList<NewsData> relevantNews=new ArrayList<NewsData>();
        //find all ids containing keyword
        ExecutorService executorService=Executors.newFixedThreadPool(10);
        for(final NewsSumData nsd:newsSumList){
            if(nsd.title.contains(keyword)){
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (relevantNews) {
                            relevantNews.add(GetNewsData(nsd.id));
                        }
                    }
                });
            }
        }
        executorService.shutdown();
        //from front to back to ensure time sequence
        //then get news corresponding to ids
        return relevantNews;

    }
    public ArrayList<NewsData> getSomeNewsData(){
        final ArrayList<NewsData> someNewsList = new ArrayList<NewsData>();
        synchronized (newsSumList) {
            while(newsSumList.size()==0){
                try {
                    newsSumList.wait();
                }catch (Exception e){

                }
            }

            System.out.println("print news data");
            //get ids of first 20 items
            ArrayList<String> ids = new ArrayList<String>();
            int length = ids.size();
            for (int i = 0; i <= 20; i++) {
                ids.add(newsSumList.get(i).id);
            }
            //get 20 items

            for (final String id : ids) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        synchronized (someNewsList) {
                            someNewsList.add(GetNewsData(id));
                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                }
            }
        }
        Message msg=new Message();
        msg.obj=someNewsList;
        mhandler.sendMessage(msg);
        return someNewsList;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return mIBinder;
    }
    public void setHandler(Handler handler){
        mhandler=handler;
    }
    //private final Messenger mMessenger = new Messenger(new MessengerHandler());
    public class LocalBinder extends Binder
    {
        public DataManager getInstance()
        {
            return DataManager.this;
        }
    }

    public ArrayList<NewsSumData> getNewsSumList() {
        return newsSumList;
    }
    //add
    public ArrayList<NewsData> getNewsReadList(){return newsReadList;}
}
