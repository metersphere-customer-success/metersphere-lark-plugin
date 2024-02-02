import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.springframework.lang.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class test {
    public String doPost(String httpUrl, @Nullable String param) {
        StringBuffer result = new StringBuffer();
        //连接
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            //创建连接对象
            URL url = new URL(httpUrl);
            //创建连接
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方法
            connection.setRequestMethod("POST");
            //设置连接超时时间
            connection.setConnectTimeout(15000);
            //设置读取超时时间
            connection.setReadTimeout(15000);
            //DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //设置是否可读取
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            //拼装参数
            if (null != param && !param.equals("")) {
                //设置参数
                os = connection.getOutputStream();
                //拼装参数
                os.write(param.getBytes("UTF-8"));
            }
            //设置权限
            //设置请求头等
            //开启连接
            //connection.connect();
            //读取响应
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                        result.append("\r\n");
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭连接
            connection.disconnect();
        }
        return result.toString();
    }
    @Test
    public void adadwawd() {
        HashMap<String,Object> queryBody = new HashMap<>();
        queryBody.put("plugin_id","MII_649A597115E54001");
        queryBody.put("plugin_secret","38F742BD4DE8A11868FBEF52087E861B");
        String message = doPost("https://project.feishu.cn/bff/v2/authen/plugin_token", JSONObject.toJSONString(queryBody));
        System.out.println(message);
    }

    public static void main(String[] args) {
        String aa = "{ \"stIntelliPickStockV2\": { \"fFirstIncrease\": 0.01861, \"fMonthIncrease\": 0.01435, \"fMonthSuccPercent\": 0.46080, \"fWeekIncrease\": 0.01336, \"iSubscriptionsCount\": 10000, \"sDate\": \"2023-07-19\", \"sDescription\": \"灯塔大数据选股模型根据消息面、技术面和资金等因素，筛选出当日热门的股票。\", \"sId\": \"99995_128\", \"sSource\": \"灯塔策略\", \"sSubscriptionsCount\": \"10000\\u0000\\u0000\\u0000人订阅\", \"sTitle\": \"灯塔金股\", \"vBrokerList\": [  ], \"vIntelliSec\": [ { \"fDayAvgIncrease\": 0.00000, \"fFirstIncrease\": 0.09963, \"fMaxIncrease\": 0.46125, \"fSelectedPrice\": 2.71000, \"fToNowIncrease\": 0.46125, \"sChnName\": \"众泰汽车\", \"sDtCode\": \"0001000980\", \"sOptime\": \"2023-07-04\" }, { \"fDayAvgIncrease\": 0.00000, \"fFirstIncrease\": 0.10029, \"fMaxIncrease\": 0.33261, \"fSelectedPrice\": 13.86000, \"fToNowIncrease\": 0.32756, \"sChnName\": \"浙江世宝\", \"sDtCode\": \"0001002703\", \"sOptime\": \"2023-07-06\" }, { \"fDayAvgIncrease\": 0.00000, \"fFirstIncrease\": 0.10280, \"fMaxIncrease\": 0.42991, \"fSelectedPrice\": 1.07000, \"fToNowIncrease\": 0.28037, \"sChnName\": \"金科股份\", \"sDtCode\": \"0001000656\", \"sOptime\": \"2023-06-21\" } ], \"vtAvgIncrease\": [ 0.01861, 0.01632, 0.01512, 0.01462, 0.01336, 0.01262, 0.01229, 0.01200, 0.01201, 0.01170, 0.01137, 0.01180, 0.01233, 0.01181, 0.01205, 0.01205, 0.01192, 0.01224, 0.01232, 0.01170, 0.01106, 0.01067, 0.01101, 0.01196, 0.01261, 0.01320, 0.01365, 0.01386, 0.01438, 0.01435, 0.01411, 0.01426, 0.01503, 0.01567, 0.01579, 0.01588, 0.01609, 0.01531, 0.01441, 0.01502, 0.01483, 0.01566, 0.01591, 0.01595, 0.01563, 0.01600, 0.01671, 0.01678, 0.01689, 0.01736, 0.01884, 0.01882, 0.01902, 0.01868, 0.01880, 0.01900, 0.01873, 0.01949, 0.01974, 0.01965 ], \"vtIntelliStock\": [ { \"fSelectedPrice\": 5.06000, \"sDtSecCode\": \"0001002251\", \"sSecName\": \"步步高\" }, { \"fSelectedPrice\": 47.60000, \"sDtSecCode\": \"0001300803\", \"sSecName\": \"指南针\" }, { \"fSelectedPrice\": 18.13000, \"sDtSecCode\": \"0101603220\", \"sSecName\": \"中贝通信\" } ], \"vtSuccPercent\": [ 0.60256, 0.57220, 0.55314, 0.54121, 0.53012, 0.52409, 0.51929, 0.50730, 0.50706, 0.50273, 0.49988, 0.49516, 0.49603, 0.49367, 0.48621, 0.48458, 0.47997, 0.48058, 0.48293, 0.47406, 0.47192, 0.46853, 0.46638, 0.46873, 0.46658, 0.46844, 0.46503, 0.46814, 0.46824, 0.46080, 0.46140, 0.46074, 0.45757, 0.46069, 0.46065, 0.45746, 0.45528, 0.45740, 0.45116, 0.45075, 0.44982, 0.44687, 0.44797, 0.44933, 0.44586, 0.44340, 0.43890, 0.44447, 0.44430, 0.44668, 0.44997, 0.44521, 0.44325, 0.43438, 0.44188, 0.44017, 0.43513, 0.44114, 0.43829, 0.44042 ], \"vtTagInfo\": [ { \"eAttiType\": 0, \"eTagType\": 1, \"sTagName\": \"消息面\" }, { \"eAttiType\": 0, \"eTagType\": 2, \"sTagName\": \"短线\" } ], \"vtYearEarning\": [ 2.32580, 1.36040, 0.94512, 0.73079, 0.55651, 0.45079, 0.38397, 0.33340, 0.30033, 0.26593, 0.23691, 0.22702, 0.22010, 0.19683, 0.18821, 0.17723, 0.16551, 0.16099, 0.15403, 0.13928, 0.12572, 0.11601, 0.11469, 0.11963, 0.12127, 0.12220, 0.12185, 0.11944, 0.11982, 0.11571, 0.11025, 0.10803, 0.11050, 0.11196, 0.10968, 0.10727, 0.10583, 0.09814, 0.09005, 0.09157, 0.08826, 0.09106, 0.09038, 0.08859, 0.08496, 0.08509, 0.08703, 0.08561, 0.08447, 0.08508, 0.09057, 0.08880, 0.08807, 0.08491, 0.08394, 0.08331, 0.08072, 0.08257, 0.08225, 0.08054 ] } }";
        JSONObject jsonObject = JSONObject.parseObject(aa);
        JSONObject stockV2 = jsonObject.getJSONObject("stIntelliPickStockV2");
        System.out.println(stockV2.toJSONString());
        String d = stockV2.getString("fFirstIncrease");
        System.out.println(d);
    }
    @Test
    public void wdawd(){
        HashMap<String,Object> queryBody = new HashMap<>();
        queryBody.put("plugin_id","MII_6389A2D6482C8002");
        queryBody.put("plugin_secret","7D31E0C5D0AF0E19186BCAD66FA3D85E");
        String result2 = HttpRequest.post("https://project.feishu.cn/bff/v2/authen/plugin_token")
                .header("Content-Type", "application/json")//头信息，多个头信息多次调用此方法即可
                .form(JSONObject.toJSONString(queryBody))//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        System.out.println(result2);
    }
}
