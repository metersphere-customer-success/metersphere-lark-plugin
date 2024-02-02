
import com.alibaba.fastjson2.JSONObject;
import com.lark.oapi.Client;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReq;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReqBody;
import com.lark.oapi.service.docx.v1.model.CreateDocumentResp;
import com.lark.oapi.service.im.v1.enums.MsgTypeEnum;
import com.lark.oapi.service.im.v1.model.ext.MessageText;
import io.metersphere.platform.api.AbstractPlatform;
import io.metersphere.platform.domain.*;
import io.metersphere.platform.impl.*;
import io.metersphere.plugin.utils.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;


public class LarkClientTest {

    public class DocxSample {

    }
    private LarkPlatform client;

    private String userKey;

    private String token;

    public void a(){

    }
    LarkConfig larkConfig = new LarkConfig();

//    @Before
//    public void loadHuenlijiClient() throws Exception {
//        PlatformRequest request = new PlatformRequest();
//        larkConfig.setUrl("https://project.feishu.cn");
//        larkConfig.setPluginId("MII_650BAC2FBD82C003");
//        larkConfig.setPluginSecret("C697BA6E84BE65FAE045E7B08A90FBE5");
//        larkConfig.setUserKey("7241406358686416900");
//        larkConfig.setSpaceId("650bab169b4ea5d284b5c5c9");
//        String inConfig = JSON.toJSONString(larkConfig);
//        request.setIntegrationConfig(inConfig);
//        client = new LarkPlatform(request);
//    }

    @Test
    public void getThirdPartCustomField_hunliji() throws Exception{
        HashMap<String,String> a = new HashMap<>();
        a.put("spaceId","617907a1c6bbf993652cb1d8");
        client.getThirdPartCustomField(JSON.toJSONString(a));
    }

//    @Before
//    public void loadLijiaClient() throws Exception {
//        PlatformRequest request = new PlatformRequest();
//        larkConfig.setUrl("https://project.feishu.cn");
//        larkConfig.setPluginId("MII_652515C12C0E4004");
//        larkConfig.setPluginSecret("66E3AC84B81809961190008AEC9C216F");
//        larkConfig.setUserKey("7241406358686416900");
//        larkConfig.setSpaceId("650bab169b4ea5d284b5c5c9");
//        String inConfig = JSON.toJSONString(larkConfig);
//        request.setIntegrationConfig(inConfig);
//        client = new LarkPlatform(request);
//    }

    @Before
    public void loadHuenlijiClient() throws Exception {
        PlatformRequest request = new PlatformRequest();
        larkConfig.setUrl("https://project.feishu.cn");
        larkConfig.setPluginId("MII_63C4D9A1E1C14003");
        larkConfig.setPluginSecret("ADEC309FEF445FD40ED830976C2E7FB5");
        larkConfig.setUserKey("7152532929715896348");
        larkConfig.setSpaceId("617907a1c6bbf993652cb1d8");
        String inConfig = JSON.toJSONString(larkConfig);

        request.setIntegrationConfig(inConfig);
        client = new LarkPlatform(request);
    }

    @Test
    public void getDemands(){
        LarkProjectConfig larkProjectConfig = new LarkProjectConfig();
        larkProjectConfig.setDemandId("12142878");
        String inConfig = JSON.toJSONString(larkProjectConfig);
        client.getDemands(inConfig);
    }

    @Test
    public void syncAllIssues(){
        SyncAllIssuesRequest syncIssuesRequest = new SyncAllIssuesRequest();
        LarkProjectConfig larkProjectConfig = new LarkProjectConfig();
        larkProjectConfig.setDemandId("15073842");
        String inConfig = JSON.toJSONString(larkProjectConfig);
        syncIssuesRequest.setProjectConfig(inConfig);
        client.syncAllIssues(syncIssuesRequest);
    }

    @Test
    public void testSI(){
        LarkSearchWorkItemRequest larkWorkItemRequest = new LarkSearchWorkItemRequest();
//        larkWorkItemRequest.setPage_num(1l);
        LarkSearchGroup larkSearchGroup = new LarkSearchGroup();
        larkSearchGroup.setConjunction("AND");
        LarkSearchParam larkSearchParam = new LarkSearchParam();
        larkSearchParam.setOperator("HAS ANY OF");
        larkSearchParam.setParam_key("_field_linked_story");
        larkSearchParam.setValue(Arrays.asList(1142750));
        larkSearchGroup.getSearch_params().add(larkSearchParam);
        larkWorkItemRequest.setSearch_group(larkSearchGroup);
        List<PlatformCustomFieldItemDTO> temp = client.getThirdPartCustomFieldIO(JSON.toJSONString(larkConfig));
        Object obj = client.searchWorkItemAll(larkWorkItemRequest,"issue");
        System.out.println(obj);
    }

    @Test
    public void tGWI(){
        LarkProjectConfig larkProjectConfig =  new LarkProjectConfig();
//        larkProjectConfig.setDemandId("1142750");
        LarkWorkItemRequest workItemRequest = new LarkWorkItemRequest(Arrays.asList("story"));
        List<LarkWorkItemInfo> larkWorkItemInfos = client.larkAbstractClient.getWorkItemAll(workItemRequest);
        client.checkIssueByDemandId(larkWorkItemInfos, larkProjectConfig.getDemandId());
        System.out.println(larkWorkItemInfos);
    }

    @Test
    public void tSWI(){
//        LarkProjectConfig larkProjectConfig =  new LarkProjectConfig();
//        larkProjectConfig.setDemandId("1142750");
//        LarkWorkItemRequest workItemRequest = new LarkWorkItemRequest(Arrays.asList("issue"));
        LarkSearchWorkItemRequest workItemRequest = new LarkSearchWorkItemRequest();
        workItemRequest.setPage_num(1l);
//        workItemRequest.setSearch_group();
        List<LarkWorkItemInfo> larkWorkItemInfos = client.larkAbstractClient.searchWorkItem(workItemRequest,"issue");
        System.out.println(larkWorkItemInfos);
    }

    @Test
    public void test01() {
//        //连接redis 必须保证redis服务可以远程连接
//        //Jedis 把每个redis命令封装成对应的方法
//        Jedis jedis = new Jedis("10.1.12.13", 6379);
//        jedis.auth("Password123@redis");
//        //对字符串的操作
//        //存储一个值
//        String s = jedis.set("k1", "v1");
//        System.out.println("返回的结果:" + s);
//        //存储一个值，时间结束后自动删除
//        String setex = jedis.setex("k2", 30l, "v2");
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
//        //关闭
//        jedis.close();
    }
    @Test
    public void ttt() {
        String daata = "\"{\\\"data\\\":[{\\\"default_value\\\":{\\\"default_appear\\\":1,\\\"value\\\":null},\\\"field_alias\\\":\\\"description\\\",\\\"field_key\\\":\\\"description\\\",\\\"field_name\\\":\\\"缺陷描述\\\",\\\"field_type_key\\\":\\\"multi_text\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"},{\\\"default_value\\\":{\\\"default_appear\\\":1,\\\"value\\\":\\\"\\\"},\\\"field_alias\\\":\\\"name\\\",\\\"field_key\\\":\\\"name\\\",\\\"field_name\\\":\\\"缺陷名称\\\",\\\"field_type_key\\\":\\\"text\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"},{\\\"default_value\\\":{\\\"default_appear\\\":2,\\\"value\\\":null},\\\"field_alias\\\":\\\"owner\\\",\\\"field_key\\\":\\\"owner\\\",\\\"field_name\\\":\\\"创建者\\\",\\\"field_type_key\\\":\\\"user\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"},{\\\"default_value\\\":{\\\"default_appear\\\":2,\\\"value\\\":null},\\\"field_alias\\\":\\\"start_time\\\",\\\"field_key\\\":\\\"start_time\\\",\\\"field_name\\\":\\\"提出时间\\\",\\\"field_type_key\\\":\\\"date\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"},{\\\"default_value\\\":{\\\"default_appear\\\":2,\\\"value\\\":null},\\\"field_alias\\\":\\\"current_status_operator\\\",\\\"field_key\\\":\\\"current_status_operator\\\",\\\"field_name\\\":\\\"当前负责人\\\",\\\"field_type_key\\\":\\\"multi_user\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"},{\\\"default_value\\\":{\\\"default_appear\\\":2,\\\"value\\\":null},\\\"field_alias\\\":\\\"updated_at\\\",\\\"field_key\\\":\\\"updated_at\\\",\\\"field_name\\\":\\\"更新时间\\\",\\\"field_type_key\\\":\\\"date\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"},{\\\"default_value\\\":{\\\"default_appear\\\":2,\\\"value\\\":null},\\\"field_alias\\\":\\\"multi_attachment\\\",\\\"field_key\\\":\\\"multi_attachment\\\",\\\"field_name\\\":\\\"多个附件\\\",\\\"field_type_key\\\":\\\"multi_file\\\",\\\"is_required\\\":2,\\\"is_validity\\\":1,\\\"is_visibility\\\":1,\\\"label\\\":\\\"\\\"}],\\\"err\\\":{},\\\"err_code\\\":0,\\\"err_msg\\\":\\\"\\\"}\"";
//        String dd = "{\"data\":\"ee\",\"err\":{},\"err_code\":0,\"err_msg\":\"\"}";
        String dd = "{\"data\":[{\"default_value\":{\"default_appear\":1,\"value\":null},\"field_alias\":\"description\",\"field_key\":\"description\",\"field_name\":\"缺陷描述\",\"field_type_key\":\"multi_text\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"},{\"default_value\":{\"default_appear\":1,\"value\":\"\"},\"field_alias\":\"name\",\"field_key\":\"name\",\"field_name\":\"缺陷名称\",\"field_type_key\":\"text\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"},{\"default_value\":{\"default_appear\":2,\"value\":null},\"field_alias\":\"owner\",\"field_key\":\"owner\",\"field_name\":\"创建者\",\"field_type_key\":\"user\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"},{\"default_value\":{\"default_appear\":2,\"value\":null},\"field_alias\":\"start_time\",\"field_key\":\"start_time\",\"field_name\":\"提出时间\",\"field_type_key\":\"date\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"},{\"default_value\":{\"default_appear\":2,\"value\":null},\"field_alias\":\"current_status_operator\",\"field_key\":\"current_status_operator\",\"field_name\":\"当前负责人\",\"field_type_key\":\"multi_user\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"},{\"default_value\":{\"default_appear\":2,\"value\":null},\"field_alias\":\"updated_at\",\"field_key\":\"updated_at\",\"field_name\":\"更新时间\",\"field_type_key\":\"date\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"},{\"default_value\":{\"default_appear\":2,\"value\":null},\"field_alias\":\"multi_attachment\",\"field_key\":\"multi_attachment\",\"field_name\":\"多个附件\",\"field_type_key\":\"multi_file\",\"is_required\":2,\"is_validity\":1,\"is_visibility\":1,\"label\":\"\"}],\"err\":{},\"err_code\":0,\"err_msg\":\"\"}";
//        LarkResponseBase larkResponseBase = JSON.parseObject(dd, LarkResponseBase.class);
//        Map<String , String> larkResponseBase = JSON.parseMap(dd);
//        System.out.println(larkResponseBase);
    }

    @Test
    public void getThirdPartCustomField() throws Exception{
        HashMap<String,String> a = new HashMap<>();
        a.put("spaceId","6389a7347efa66176062c4fc");
        a.put("demandId","227635");
        client.getThirdPartCustomField(JSON.toJSONString(a));
    }

    @Test
    public void getStatusList(){
        List<PlatformStatusDTO> t = client.getStatusList("aaa");
        System.out.println(JSON.toJSONString(t));
    }
    @Test
    public void checkCommentInfo() {
        try {
            client.validateIntegrationConfig();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void ddd(){
        Object a = "[\"7152532929715896348\"]";
        a = (a+"").replace("\"","");
        List<String> temp = JSON.parseArray(a+"");
        System.out.println(temp);
    }

    @Test
    public void getProjectOptions() {
        GetOptionRequest getOptionRequest = new GetOptionRequest();
        getOptionRequest.setOptionMethod("aa");
        System.out.println(client.getProjectOptions(getOptionRequest));
    }

    @Test
    public void v() {
       Object a = 123;
        List<String> temp = new ArrayList<>();
        temp.add(a+"");
        Object b = temp;
        System.out.println(b);
    }

    @Test
    public void getIssueTypes(){
        GetOptionRequest getOptionRequest = new GetOptionRequest();
        getOptionRequest.setProjectConfig("6364c89a48cdc29cab10c396");
        List<SelectOption> selectOptions = client.getIssueTypes(getOptionRequest);
        System.out.println(JSON.toJSONString(selectOptions));
    }

    @Test
    public void addIssues(){
        PlatformIssuesUpdateRequest platformIssuesUpdateRequest = new PlatformIssuesUpdateRequest();
        platformIssuesUpdateRequest.setUserPlatformUserConfig("{\"accessKeyID\":\"LTAI5tFjnEKwhV8oW7LdqHBC\",\"accessKeySecret\":\"vbZpjooLytNvGpVGllsMLtAGvddrMk\",\"accountId\":\"1212517348583146\",\"pluginId\":\"MII_63C4D9A1E1C14003\",\"pluginSecret\":\"ADEC309FEF445FD40ED830976C2E7FB5\",\"userKey\":\"7152532929715896348\"}");
        platformIssuesUpdateRequest.setProjectConfig("{\"demandId\":\"15073842\",\"jiraKey\":null,\"tapdId\":null,\"azureDevopsId\":null,\"zentaoId\":null,\"thirdPartTemplate\":true}");
        platformIssuesUpdateRequest.setProjectId("74879a0b-aef4-4091-98bb-e81e7275e6a6");
        platformIssuesUpdateRequest.setCreator("admin");
        List<PlatformCustomFieldItemDTO> customFieldList = new ArrayList<>();
        PlatformCustomFieldItemDTO p = new PlatformCustomFieldItemDTO();
        p.setValue("62a69ddd9afa9ae828ba1265");
        p.setCustomData("business");
        p.setId("business");
        p.setType("select");
        p.setName("产品线");
        customFieldList.add(p);
        platformIssuesUpdateRequest.setCustomFieldList(customFieldList);
        client.addIssue(platformIssuesUpdateRequest);
    }

    @Test
    public void getThirdPartCustomFieldIO(){
        LarkProjectConfig larkProjectConfig = new LarkProjectConfig();
        larkProjectConfig.setDemandId("6711729");
        String inConfig = JSON.toJSONString(larkProjectConfig);
        client.getThirdPartCustomFieldIO(inConfig);
    }
}
