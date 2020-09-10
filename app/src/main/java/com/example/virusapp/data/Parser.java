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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.*;



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
        System.out.println("begin virus data");
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
        while (fieldsIterator.hasNext())
        {
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
            if(local.country.equals("China")&&(local.county.equals(""))){
               // System.out.println(local.province);
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
            JsonNode dailyDataNode=value.get("data");
            assert dailyDataNode.isArray();
            JsonNode dailyNode=dailyDataNode.get(dailyDataNode.size()-1);//get the last item
            local.lastDayData=new DailyData();
            local.lastDayData.confirmed=dailyNode.get(0).asInt();
            local.lastDayData.suspected=dailyNode.get(1).asInt();
            local.lastDayData.cured=dailyDataNode.get(2).asInt();
            local.lastDayData.dead=dailyDataNode.get(3).asInt();

            localDataList.add(local);
        }
        //here try

        //System.out.println(localDataList.get(0));
        System.out.println("end virus data");
        return localDataList;
    }
    public ArrayList<NewsSumData> getVirusNewsList(){
        System.out.println("begin virus news");
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

            //System.out.println(newsSumData);

            newsSumList.add(newsSumData);
        }
        System.out.println("end virus news");
        return newsSumList;
    }
    public ArrayList<NewsSumData> getLatestNewsList(){
        System.out.println("begin latest virus news");
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
        for(int i=arrayNode.size()-20;i<arrayNode.size();i++) {
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

            //System.out.println(newsSumData);

            newsSumList.add(newsSumData);
        }
        System.out.println("end latest virus news");
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
        //System.out.println(newsData);
        return newsData;
    }
    public ArrayList<Entity> getVirusEntity(final String name){
        String dataJson=httpGet("https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity="+name);
        JsonNode rootNode=null;
        try {
            rootNode = mapper.readTree(dataJson);
        }
        catch(Exception e){
            System.out.println(e);
        }
        ArrayList<Entity> entities=new ArrayList<Entity>();
        JsonNode arrayNode=rootNode.get("data");
        assert arrayNode.isArray();

        for(int i=0;i<arrayNode.size();i++) {
            JsonNode entityNode=arrayNode.get(i);

            Entity entity=new Entity();
            entity.hot=entityNode.get("hot").asDouble();
            entity.label=entityNode.get("label").asText();
            entityNode=entityNode.get("abstractInfo");
            if(!entityNode.get("baidu").asText().equals("")){
                entity.info=entityNode.get("baidu").asText();
            }
            else if(!entityNode.get("zhwiki").asText().equals("")){
                entity.info=entityNode.get("zhwiki").asText();
            }
            else if(!entityNode.get("zhwiki").asText().equals("")){
                entity.info=entityNode.get("enwiki").asText();
            }
            else
                entity.info="";
            JsonNode infoNode=entityNode.get("COVID");
            JsonNode propertyNode=infoNode.get("properties");
            HashMap<String,String> properties=new HashMap<String,String>();
            Iterator<Map.Entry<String,JsonNode>> fieldsIterator = propertyNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                properties.put(field.getKey(),field.getValue().asText());
            }
            entity.properties=properties;

            JsonNode relationNode=infoNode.get("relations");
            ArrayList<Relation> relations=new ArrayList<Relation>();
            for(int j=0;j<relationNode.size();j++){
                JsonNode rNode=relationNode.get(j);
                Relation r=new Relation();
                r.forward=rNode.get("forward").asBoolean();
                r.label=rNode.get("label").asText();
                r.relation=rNode.get("relation").asText();
                relations.add(r);
            }
            entity.relations=relations;
            //System.out.println(entity);
            entities.add(entity);
        }
        return entities;//shiti,guanxi,shuxing
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
