package io.metersphere.platform.commons;


import io.metersphere.plugin.utils.JSON;

import java.util.Map;

public enum ERRCODEEnum {

    ERROR_10001(10001,"没有操作权限\n" +
            "更新评论：当前操作人不是评论创建人\n" +
            "更新或删除视图：没有编辑视图的权限\n" +
            "创建子任务：子任务所在空间和路径空间不匹配\n" +
            "节点完成/状态流转：没有权限完成\n" +
            "获取指定的工作项列表：未在对应空间安装插件"),
    ERROR_10002(10002,"非法操作\n" +
            "节点完成/状态流转：当前节点/状态不能流转到指定节点/状态"),
    ERROR_10003(10003,"暂未开放"),
    ERROR_10004(10004,"操作失败\n" +
            "该操作会导致流程图节点消失，而流程图至少需存在一个节点"),
    ERROR_10021(10021,"请求头未传plugin_token"),
    ERROR_10022(10022,"plugin_token校验失败"),
    ERROR_10211(10211,"plugin_token信息不合法\n" +
            "插件token错误，无法解析出具体信息\n" +
            "如果是插件级token，未传X-USER-KEY"),
    ERROR_10301(10301,"plugin_token权限校验未通过\n" +
            "没申请当前操作接口的api权限\n" +
            "申请权限了，但是未发布版本或重新发布版本\n" +
            "发布版本了，但是未安装或更新插件\n" +
            "空间ID不存在"),
    ERROR_10302(10302,"用户已离职"),
    ERROR_10404(10404,"当前操作用户没有空间访问权限"),
    ERROR_10429(10429,"接口请求过于频繁，超过同一token请求同一接口 15qps 限制"),
    ERROR_10430(10430,"接口请求幂等限制，可排查header中X-IDEM-UUID幂等串是否冲突"),
    ERROR_20001(20001,"获取空间详情：传入的空间id超过最大20的限制"),
    ERROR_20002(20002,"查询评论：page_size超过最大200的限制"),
    ERROR_20003(20003,"获取指定的工作项列表：work_item_type_keys未填"),
    ERROR_20004(20004,"获取指定的工作项列表：user_keys超过最大10的限制"),
    ERROR_20005(20005,"必填的请求参数未填"),
    ERROR_20006(20006,"请求参数不合法，请检查参数与文档是否匹配\n" +
            "如果是创建子任务，提示 target node with role owner config, not support specify 'assignee'\"，表明节点负责人和角色绑定，需通过role_assignee字段指定负责人"),
    ERROR_20007(20007,"工作项已经被终止"),
    ERROR_20008(20008,"工作项已经被恢复"),
    ERROR_20009(20009,"终止/恢复工作项，缺失了原因"),
    ERROR_20010(20010,"创建、更新视图：传入的工作项id列表不是同一种工作项类型"),
    ERROR_20011(20011,"删除固定视图：目前只支持删除固定视图，想要删除的视图不是固定视图"),
    ERROR_20012(20012,"视图的id所属空间不属于参数中的空间"),
    ERROR_20013(20013,"获取指定的工作项列表：时间相关参数，不是毫秒时间戳(13位数字)"),
    ERROR_20014(20014,"工作项所属空间和传入的空间不匹配"),
    ERROR_20015(20015,"存在相同的字段出现在需要返回列表(Without '-') 和 不需要返回列表(With '-')"),
    ERROR_20016(20016,"节点未到达，无法进行节点流转"),
    ERROR_20017(20017,"节点已经完成，无法再次完成"),
    ERROR_20018(20018,"节点不存在当前工作项的节点流配置中"),
    ERROR_20019(20019,"拉取群的机器人数量超过5个"),
    ERROR_20020(20020,"未填拉取群的机器人"),
    ERROR_20021(20021,"群id不属于参数中的工作项"),
    ERROR_20022(20022,"暂未开放"),
    ERROR_20023(20023,"暂未开放"),
    ERROR_20024(20024,"上传文件大小限制100M"),
    ERROR_20025(20025,"字段的key和对接标识都缺失"),
    ERROR_20026(20026,"查询的工作项是状态流工作项"),
    ERROR_20027(20027,"查询的工作项是节点流工作项"),
    ERROR_20028(20028,"传入的工作项id数量限制50"),
    ERROR_20029(20029,"不支持更新的字段类型(字段key会在msg中返回)"),
    ERROR_20030(20030,"暂未开放"),
    ERROR_20031(20031,"暂未开放"),
    ERROR_20032(20032,"差异化排期未指定用户更新排期，或者指定的用户超过一个"),
    ERROR_20033(20033,"不能更新该字段"),
    ERROR_20037(20037,"节点未完成，无法回滚"),
    ERROR_20038(20038,"节点完成/状态流转时，必填字段未填写"),
    ERROR_20039(20039,"使用的是应用token，请求头中必须带上X-USER-KEY"),
    ERROR_20040(20040,"上传附件时，传入的表单是空(content-type未选择multipart/form-data)"),
    ERROR_20041(20041,"创建工作项必须传入role_owners字段"),
    ERROR_20042(20042,"未填X-User-Key或者传入的user_key错误"),
    ERROR_20043(20043,"查询视图列表时，最多只能传入10个视图id"),
    ERROR_20044(20044,"工作项已被禁用，无法查询到元数据"),
    ERROR_20045(20045,"评论不属于指定的工作项"),
    ERROR_20046(20046,"工作流中不存在该子任务"),
    ERROR_20047(20047,"更新或创建子任务，role_assignee和assignee不能同时传入"),
    ERROR_20048(20048,"更新或创建子任务时，传入的role_assignee里面的role与节点绑定的role不匹配"),
    ERROR_20049(20049,"仅渠道用户使用，填入的TenantGroupId错误"),
    ERROR_20050(20050,"更新或创建工作项时，传入的字段选项值错误"),
    ERROR_20051(20051,"填入的field_linked_story字段值错误"),
    ERROR_20052(20052,"缺陷的operator角色负责人填写错误，可对比获取工作项详情接口返回值的role_owners字段"),
    ERROR_20053(20053,"缺陷的reporter角色负责人填写错误，可对比获取工作项详情接口返回值的role_owners字段"),
    ERROR_20055(20055,"查询结果超过2000个，请重新设置筛选条件"),
    ERROR_20056(20056,"只有工作流模式可以终止和恢复"),
    ERROR_20057(20057,"搜索时，传入的ProjectKeys和SimpleNames的并集不能超过10个"),
    ERROR_20058(20058,"搜索时，在SearchUser这个结构中，Role和FieldKey不能同时出现"),
    ERROR_20059(20059,"搜索时，在SearchUser这个结构中，UserKeys如果为空，Role或FieldKey不能单独传入"),
    ERROR_20060(20060,"工作项类型或关联工作项类型，与关联关系的配置不匹配"),
    ERROR_20061(20061,"指定关联关系的字段类型，不是关联类型"),
    ERROR_20062(20062,"指定的RelationType不存在，目前只支持0：字段key,1：字段alias"),
    ERROR_20063(20063,"搜索的操作错误，不同的参数可使用的操作符不同"),
    ERROR_20064(20064,"搜索指定的选项个数超过限制，最多可传入50个选项值"),
    ERROR_20065(20065,"当前参数不支持状态流筛选"),
    ERROR_20066(20066,"搜索系统外信号，当操作符是=或者!=时，数组长度只能是1"),
    ERROR_20067(20067,"搜素系统外信号，传入的值不支持筛选"),
    ERROR_20068(20068,"指定的参数不支持筛选"),
    ERROR_20069(20069,"搜索传入的参数值异常"),
    ERROR_20070(20070,"上传附件时指定的字段已失效"),
    ERROR_20071(20071,"搜索指定参数是people时，不支持缺陷工作项"),
    ERROR_20072(20072,"搜索的Conjunction仅支持且、或"),
    ERROR_20080(20080,"综搜查询中query必填同时长度限制小于200"),
    ERROR_20081(20081,"综搜查询中目前仅支持查询工作项和视图"),
    ERROR_20082(20082,"子任务状态更新接口中只有回滚和确认操作"),
    ERROR_20083(20083,"创建工作项时，传入的字段重复"),
    ERROR_30005(30005,"工作项未找到，可能是工作项已删除、查询的工作项id不正确、查询的工作项类型和工作项id不匹配"),
    ERROR_30007(30007,"工作项中未找到节点流"),
    ERROR_30008(30008,"空间下的业务线未找到"),
    ERROR_30009(30009,"字段未在字段配置中，无法更新或创建"),
    ERROR_30010(30010,"工作项中未找到状态流"),
    ERROR_30011(30011,"节点在工作项的节点流配置中未找到"),
    ERROR_30012(30012,"状态在工作项的状态流配置中未找到"),
    ERROR_30013(30013,"模板未找到"),
    ERROR_30014(30014,"work_item_type_key未找到"),
    ERROR_30015(30015,"未找到这条记录"),
    ERROR_30016(30016,"传入的project_key未找到对应空间"),
    ERROR_30017(30017,"role_owners字段未设置，或者当前操作导致role_owners消失"),
    ERROR_30018(30018,"关联关系key在配置中不存在"),
    ERROR_30006(30006,"用户密钥错误");

    private int code;
    private String message;

    ERRCODEEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getCodeInfo(String message){
        Map<String,Object> map = JSON.parseMap(message);
        ERRCODEEnum[] arr = ERRCODEEnum.values();
        Integer code = Integer.parseInt(map.get("err_code")+"");
        for(int i = 0; i < arr.length ;i++){
            if(code.intValue() == arr[i].code){
                return arr[i].message;
            }
        }
        return message;
    }

//    public static String getCode


    public static String getCodeInfoByCode(int code){
        ERRCODEEnum[] arr = ERRCODEEnum.values();
        for(int i = 0; i < arr.length ;i++){
            if(code == arr[i].code){
                return arr[i].message;
            }
        }
        return code+"";
    }

}
