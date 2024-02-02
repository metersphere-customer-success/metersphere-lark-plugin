package io.metersphere.platform.singleton;

import com.google.gson.Gson;
import io.metersphere.platform.domain.LarkRedisPCFID;
import io.metersphere.platform.domain.PlatformCustomFieldItemDTO;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RedisSingleton {

    public final static String TIMEOUT = "TIMEOUT";

    private static final RedisSingleton INSTANCE = new RedisSingleton();

    private RedisSingleton() {}

    private Gson gson = new Gson();


    public synchronized static RedisSingleton getInstance() {
        return INSTANCE;
    }

    private Jedis getJedis(){
        Map<String, String> envMap = System.getenv();
        String redisHost = envMap.get("REDIS_HOST");
        String redisPort = envMap.get("REDIS_PORT");
        String redisPasswrod = envMap.get("REDIS_PASSWORD");
        Jedis jedis = new Jedis(redisHost, Integer.parseInt(redisPort));
        String code = jedis.auth(redisPasswrod);
        return jedis;
    }

    public String getValue(String key,String projectId){
        // json 可能等于三个值，null TIMEOUT object
        LarkRedisPCFID larkRedisPCFID = getLarkRedisPCFIDValue(key, projectId);
        if (larkRedisPCFID == null) {
            return null;
        }
        long cha = new Date().getTime() - larkRedisPCFID.getTime();
        long time = 1000 * 60 * 60l;
        if (cha > time) {
            return this.TIMEOUT;
        }
        return gson.toJson(larkRedisPCFID.getPlatformCustomFieldItemDTOList());
    }

    public LarkRedisPCFID getLarkRedisPCFIDValue(String key, String projectId){

        LarkRedisPCFID larkRedisPCFID = null;
        Jedis jedis = getJedis();
        try{
            String value = jedis.get(key);
            if(StringUtils.isBlank(value)){
                return null;
            }
            System.out.println(value+"    !!!!!");
            larkRedisPCFID = gson.fromJson(value, LarkRedisPCFID.class);
//            larkRedisPCFID = JSON.parseObject(value, LarkRedisPCFID.class);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //关闭
            jedis.close();
        }
        return larkRedisPCFID;
    }

    public void setValue(String key ,List<PlatformCustomFieldItemDTO> object, String projectId){
        Jedis jedis = getJedis();
        try{
            //存储一个值，时间结束后自动删除
            int time = 1000 * 60 * 60 * 24;
            LarkRedisPCFID larkRedisPCFID = new LarkRedisPCFID();
            larkRedisPCFID.setTime(new Date().getTime());
            larkRedisPCFID.setPlatformCustomFieldItemDTOList(object);
            String str = gson.toJson(larkRedisPCFID);
//            String str = JSON.toJSONString(larkRedisPCFID);
            jedis.setex(key, time, str);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //关闭
            jedis.close();
        }

    }


//    public void Test(){
//
//        //对字符串的操作
//        //存储一个值
//        String s = jedis.set("k1", "v1");
//        System.out.println("返回的结果:" + s);
//
//        //存储一个值，时间结束后自动删除
//        String setex = jedis.setex("k2", 30l, "v2");
//
//        System.out.println("返回的结果:" + setex);
//        //存储一个值，若已存在则不存
//        Long aLong = jedis.setnx("k3", "v3");
//        System.out.println("返回的结果:" + aLong);
//
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//
//        //对hash操作
//        Long hset = jedis.hset("k4", "name", "张三");
//        System.out.println("返回的值:" + hset);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("name", "李四");
//        Long hset1 = jedis.hset("k5", map);
//        System.out.println(hset1);
//
//
//    }


}