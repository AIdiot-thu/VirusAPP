package com.example.virusapp.data;

import com.example.virusapp.ui.news.News;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.*;
class DailyData{
    //NOTE:SEVERE”,”RISK”,”inc24” can be deprecated
    public int confirmed,suspected,cured,dead,servere,risk,inc24;
}
class LocalData{
    String country,province,county;
    int yearBegin,mouthBegin,dayBegin;
    String dailyDataList;
    //TODO finish list
    //ArrayList<DailyData> dailyDataList;

    @Override
    public String toString() {
        return "LocalData{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", county='" + county + '\'' +
                ", yearBegin=" + yearBegin +
                ", mouthBegin=" + mouthBegin +
                ", dayBegin=" + dayBegin +
                ", dailyDataList='" + dailyDataList + '\'' +
                '}';
    }
}
class NewsSumData{
    String id,type,title,lang;
    int timeYear,timeMouth,timeDay;

    @Override
    public String toString() {
        return "NewsSumData{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", lang='" + lang + '\'' +
                ", timeYear=" + timeYear +
                ", timeMouth=" + timeMouth +
                ", timeDay=" + timeDay +
                '}';
    }
}
class NewsData{
    //since we can only get newsdata by news summary,attributes like id and type are not included
    String time,title,source,content;

    @Override
    public String toString() {
        return "NewsData{" +
                "time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
/** pull data form internet and return structural data
 * @author Li
 * */
public class Parser {
    JsonFactory factory;
    ObjectMapper mapper;
    public Parser(){
        factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
    }

    //different methods for different requires

    /**real-time epidemic data
     *
     * @return ArrayList<LocalData>
     */
    //primarily Tested
    public ArrayList<LocalData> getVirusData(){
        String dataJson=httpGet("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
        //jackson to parse data,see https://stackoverflow.com/questions/19760138/parsing-json-in-java-without-knowing-json-format
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode=null;
        try {
            rootNode = mapper.readTree(dataJson);
        }
        catch(Exception e){
            System.out.println(e);
        }

        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
        //convert json to localdata and save in a local data list
        ArrayList<LocalData> localDataList=new ArrayList<LocalData>();
        int counter=0;
        while (fieldsIterator.hasNext())
        {
            counter+=1;
            Map.Entry<String,JsonNode> field = fieldsIterator.next();
            //parse each item
            LocalData local=new LocalData();
            //parse key
            String key=field.getKey();
            Pattern r = Pattern.compile("[^|]+");
            Matcher match=r.matcher(key);
            match.find();
            local.country= match.group(0);
            if(match.find()){
                local.province=match.group(0);
                if(match.find()){
                    local.county=match.group(0);
                }
            }

            //parse value
            JsonNode value=field.getValue();
            String date=value.get("begin").asText();
            try {
                local.yearBegin = Integer.parseInt(date.substring(0,4));
                local.mouthBegin = Integer.parseInt(date.substring(5, 7));
                local.dayBegin = Integer.parseInt(date.substring(8, 10));
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            //TODO parse in details
            local.dailyDataList=value.get("data").toString();
            localDataList.add(local);
        }

        //System.out.println(localDataList.get(0));
        return localDataList;
    }
    public ArrayList<NewsSumData> getVirusNewsList(){
        String dataJson=httpGet("https://covid-dashboard.aminer.cn/api/dist/events.json");
        JsonNode rootNode=null;
        try {
            rootNode = mapper.readTree(dataJson);
        }
        catch(Exception e){
            System.out.println(e);
        }
        JsonNode arrayNode=rootNode.get("datas");
        assert arrayNode.isArray();
        //iterate news list
        ArrayList<NewsSumData> newsSumList=new ArrayList<NewsSumData>();
        System.out.println("total news:"+arrayNode.size());
        for(int i=0;i<arrayNode.size();i++) {
            JsonNode newsNode=arrayNode.get(i);
            NewsSumData newsSumData=new NewsSumData();
            newsSumData.title=newsNode.get("title").asText();
            newsSumData.id=newsNode.get("_id").asText();
            newsSumData.type=newsNode.get("type").asText();
            newsSumData.lang=newsNode.get("lang").asText();
            String timeString=newsNode.get("time").asText();
            newsSumData.timeYear=Integer.parseInt(timeString.substring(0,4));
            newsSumData.timeMouth=Integer.parseInt(timeString.substring(5,7));
            newsSumData.timeDay=Integer.parseInt(timeString.substring(8,10));

            System.out.println(newsSumData);

            newsSumList.add(newsSumData);
            break;
        }

        return newsSumList;
    }
    public NewsData getVirusNews(String id){
        id="5f55ef089fced0a24b31dab8";
        String dataJson=httpGet("https://covid-dashboard-api.aminer.cn/event/"+id);
        JsonNode rootNode=null;
        try {
            rootNode = mapper.readTree(dataJson);
        }
        catch(Exception e){
            System.out.println(e);
        }
        rootNode=rootNode.get("data");

        NewsData newsData=new NewsData();
        newsData.content=rootNode.get("content").asText();
        newsData.source=rootNode.get("source").asText();
        newsData.title=rootNode.get("title").asText();
        newsData.time=rootNode.get("time").asText();
        System.out.println(newsData);
        return newsData;
    }
    public String getVirusEntity(){
        return "";
    }
    public String getVirusScholar(){
        return "";
    }
    public String httpGet(String httpurl){
        String result="";
        InputStream is = null;
        BufferedReader br = null;
        HttpURLConnection connection=null;
        try {
            URL url = new URL(httpurl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        }
        catch(java.net.MalformedURLException e){
            System.out.println(e);
        }
        catch(java.io.IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
        finally{
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();
        }
        //System.out.println(result);
        return result;
    }

}
