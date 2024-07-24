package io.metersphere.platform.impl;

//import Lincense.CheckLicense;
//import io.metersphere.base.domain.IssuesWithBLOBs;
import io.metersphere.base.domain.IssuesWithBLOBs;
import io.metersphere.platform.commons.FieldTypeMapping;
import io.metersphere.platform.constants.CustomFieldType;
import io.metersphere.platform.domain.*;
import io.metersphere.platform.api.AbstractPlatform;
import io.metersphere.plugin.exception.MSPluginException;
import io.metersphere.plugin.utils.JSON;
import io.metersphere.plugin.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LarkPlatform extends AbstractPlatform {

    public LarkAbstractClient larkAbstractClient;

    public LarkPlatform(PlatformRequest request){
//        CheckLicense.checkLicenseByRedis();
        super.key = LarkPlatformMetaInfo.KEY;
        super.request = request;
        larkAbstractClient = new LarkAbstractClient();
        setConfig();
    }



    @Override
    public void validateUserConfig(String request) {
        LarkConfig config = JSON.parseObject(request ,LarkConfig.class);
        larkAbstractClient.PLUGIN_ID = config.getPluginId();
        larkAbstractClient.PLUGIN_SECRET = config.getPluginSecret();
        larkAbstractClient.USER_KEY = config.getUserKey();
        validateIntegrationConfig();
    }

    public LarkConfig setConfig() {
        LarkConfig config = getIntegrationConfig(LarkConfig.class);
        larkAbstractClient.setConfig(config);
        return config;
    }

    protected SimpleDateFormat sdfWithZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public List<DemandDTO> getDemands(String projectConfig) {
        LarkProjectConfig lpc = JSON.parseObject(projectConfig, LarkProjectConfig.class);
        LarkWorkItemRequest larkWorkItemRequest = new LarkWorkItemRequest(Arrays.asList("story"));
        List<LarkWorkItemInfo> larkWorkItemInfos = larkAbstractClient.getWorkItemAll(larkWorkItemRequest);
        List<DemandDTO> demandDTOS = new ArrayList<>();
        for(LarkWorkItemInfo item : larkWorkItemInfos){
            if(StringUtils.equals(item.getId()+"", lpc.getDemandId())){
                demandDTOS.add(larkWorkItemToDemands(item));
            }
        }
        return demandDTOS;
    }
    private DemandDTO larkWorkItemToDemands(LarkWorkItemInfo larkWorkItemInfo){
        DemandDTO demandDTO = new DemandDTO();
        demandDTO.setId(larkWorkItemInfo.getMSId());
        demandDTO.setName(larkWorkItemInfo.getName());
        demandDTO.setPlatform("Lark");
        return demandDTO;
    }

    public void refreshUserToken(String userConfig) {
        if(StringUtils.isNotEmpty(userConfig)){
            LarkUserPlatformUserConfig larkUserPlatformUserConfig = null;
            try{
                larkUserPlatformUserConfig = JSON.parseObject(userConfig, LarkUserPlatformUserConfig.class);
            }catch (Exception e){
                return;
            }
            if(StringUtils.isBlank(larkUserPlatformUserConfig.getPluginId())
             || StringUtils.isBlank(larkUserPlatformUserConfig.getPluginSecret())
             || StringUtils.isBlank(larkUserPlatformUserConfig.getUserKey())){
                MSPluginException.throwException("请填写完整的个人信息第三方账号");
            }
            larkAbstractClient.PLUGIN_ID = larkUserPlatformUserConfig.getPluginId();
            larkAbstractClient.PLUGIN_SECRET = larkUserPlatformUserConfig.getPluginSecret();
            larkAbstractClient.USER_KEY = larkUserPlatformUserConfig.getUserKey();
            larkAbstractClient.getToken();
        } else {
            MSPluginException.throwException("");
        }
    }

    @Override
    public IssuesWithBLOBs addIssue(PlatformIssuesUpdateRequest issuesRequest) {
        refreshUserToken(issuesRequest.getUserPlatformUserConfig());
        IssuesWithBLOBs issues = null;
        try {
            issues = larkAbstractClient.addIssue(issuesRequest);
        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e.getMessage());
        }
        return issues;
    }

    @Override
    public List<SelectOption> getProjectOptions(GetOptionRequest request) {
        List<SelectOption> selectOptions = null;
        try {
            List<String> idList = larkAbstractClient.getWorkSpaceIdList();
            selectOptions = larkAbstractClient.getProjectsDetail(idList);
        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e.getMessage());
        }
        return selectOptions;
    }

    @Override
    public IssuesWithBLOBs updateIssue(PlatformIssuesUpdateRequest request) {
        refreshUserToken(request.getUserPlatformUserConfig());
        return larkAbstractClient.updateIssue(request);
    }

    @Override
    public void deleteIssue(String id) {
        larkAbstractClient.deleteIssue(id);
    }

    @Override
    public void validateIntegrationConfig() {
        larkAbstractClient.auth();
        larkAbstractClient.checkUserByUserKey();
        larkAbstractClient.checkSpaceId();
    }

    public List<LarkWorkItemInfo> searchWorkItemAll(LarkSearchWorkItemRequest larkWorkItemRequest,String type){
        return larkAbstractClient.searchWorkItemAll(larkWorkItemRequest, type);
    }


    @Override
    public void validateProjectConfig(String projectConfig) {
        try {
            LarkProjectConfig larkProjectConfig = JSON.parseObject(projectConfig, LarkProjectConfig.class);
            //婚礼纪以需求去分项目，非标，个性化的
//            List<String> spaceIds = larkAbstractClient.getWorkSpaceIdList();
            LarkWorkItemRequest larkWorkItemRequest = new LarkWorkItemRequest(Arrays.asList("story"));
            larkWorkItemRequest.setWork_item_ids(Arrays.asList(new Long[]{Long.valueOf(Long.parseLong(larkProjectConfig.getDemandId()))}));
            List<LarkWorkItemInfo> larkWorkItemInfos = larkAbstractClient.getWorkItemAll(larkWorkItemRequest);
            List<String> strings = new ArrayList<>();
            for(LarkWorkItemInfo item : larkWorkItemInfos){
                strings.add(String.valueOf(item.getId()));
            }
            if(!strings.contains(larkProjectConfig.getDemandId())){
                MSPluginException.throwException("无效的需求id");
            }
        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e.getMessage());
        }
    }

    public List<SelectOption> getIssueTypes(GetOptionRequest request) {
        List<LarkIssueType> larkIssueTypes = null;
        try {
            larkIssueTypes = larkAbstractClient.getIssueTypes(request.getProjectConfig());
        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e.getMessage());
        }
        List<SelectOption> selectOptions = larkIssueTypes.stream()
                .map(item ->  new SelectOption(item.getName(), item.getType_key()))
                .collect(Collectors.toList());
        return selectOptions;
    }


    @Override
    public boolean isAttachmentUploadSupport() {
        return true;
    }

    public List<PlatformUser> getPlatformUser() {
        return null;
    }

    private List<IssuesWithBLOBs> getMSAddIssues (List<LarkWorkItemInfo> larkWorkItemInfos, List<PlatformIssuesDTO> platformIssuesDTOS, List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOHashList) {
        List<IssuesWithBLOBs> issuesWithBLOBsList = new ArrayList<>();
        for(LarkWorkItemInfo item: larkWorkItemInfos){
            try{
                boolean isAdd = true;
                if(platformIssuesDTOS != null){
                    for(PlatformIssuesDTO p : platformIssuesDTOS){
                        if(StringUtils.equals(p.getPlatformId(), item.getMSId())){
                            isAdd = false;
                            break;
                        }
                    }
                }
                if(isAdd){
                    IssuesWithBLOBs issues = item.toIssuesWithBLOB(larkAbstractClient.USER_KEY, platformCustomFieldItemDTOHashList);
                    issuesWithBLOBsList.add(issues);
                }
            }catch (Exception e){
                System.out.println("getMSAddIssues item "+JSON.toJSONString(item));
                e.printStackTrace();
            }
        }
        return issuesWithBLOBsList;
    }

    private List<IssuesWithBLOBs> getMSUpdateIssues (List<LarkWorkItemInfo> larkWorkItemInfos, List<PlatformIssuesDTO> platformIssuesDTOS, List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOHashList) {
        List<IssuesWithBLOBs> issuesWithBLOBsList = new ArrayList<>();
        for(LarkWorkItemInfo item: larkWorkItemInfos){
            boolean isUpdate = false;
            if(platformIssuesDTOS != null) {
                for (PlatformIssuesDTO p : platformIssuesDTOS) {
                    if (StringUtils.equals(p.getPlatformId(), item.getMSId())) {
                        if (p.getUpdateTime() != item.getUpdated_at()) {
                            isUpdate = true;
                            break;
                        }
                    }
                }
            }
            if(isUpdate) {
                IssuesWithBLOBs issues = item.toIssuesWithBLOB(larkAbstractClient.USER_KEY, platformCustomFieldItemDTOHashList);
                issuesWithBLOBsList.add(issues);
            }
        }
        return issuesWithBLOBsList;
    }

    private List<String> getMSDeleteIssues (List<LarkWorkItemInfo> larkWorkItemInfos, List<PlatformIssuesDTO> platformIssuesDTOS) {
        List<String> ids = new ArrayList<>();
        for(PlatformIssuesDTO p : platformIssuesDTOS){
            boolean isDelete = true;
            for(LarkWorkItemInfo item: larkWorkItemInfos){
                if(StringUtils.equals(item.getProject_key()+"_"+item.getId(), p.getPlatformId())){
                    isDelete = false;
                    break;
                }
            }
            if(isDelete){
                ids.add(p.getId());
            }
        }
        return ids;
    }

    public void getMSAddAttachment(Map<String, List<PlatformAttachment>> attachmentMap, List<LarkWorkItemInfo> larkWorkItemInfos, List<IssuesWithBLOBs> issues) {
        for(IssuesWithBLOBs p : issues){
            for(LarkWorkItemInfo item: larkWorkItemInfos){
                if(StringUtils.equals(item.getMSId(), p.getPlatformId())){
                    attachmentMap.put(item.getMSId(), new ArrayList<>());
                    List<LarkFieldValuePairs> larkFieldValuePairsList = item.getFields();
                    for(LarkFieldValuePairs larkFieldValuePairs : larkFieldValuePairsList){
                        if(StringUtils.equals(larkFieldValuePairs.getField_type_key(), "multi_file")){
                            if(larkFieldValuePairs.getField_value() == null){
                                break;
                            }
                            String arrayStr = JSON.toJSONString(larkFieldValuePairs.getField_value());
                            List list = JSON.parseArray(arrayStr);
                            for(int i = 0; i < list.size(); i++){
                                String valueStr = JSON.toJSONString(list.get(i));
                                Map<String, String> map = JSON.parseMap(valueStr);
                                if(map != null){
                                    PlatformAttachment syncAttachment = new PlatformAttachment();
                                    // name 用于查重
                                    syncAttachment.setFileName(map.get("name"));
                                    // key 用于获取附件内容
                                    syncAttachment.setFileKey(map.get("url"));
                                    attachmentMap.get(item.getMSId()).add(syncAttachment);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private Map<String, List<PlatformAttachment>> getMSAddAttachment (List<LarkWorkItemInfo> larkWorkItemInfos, SyncIssuesResult platformIssuesDTOS) {
        Map<String, List<PlatformAttachment>> attachmentMap = new HashMap<>();
        //更新待添加的缺陷附件
        getMSAddAttachment(attachmentMap, larkWorkItemInfos, platformIssuesDTOS.getAddIssues());
        //更新待更新的缺陷附件
        getMSAddAttachment(attachmentMap, larkWorkItemInfos, platformIssuesDTOS.getUpdateIssues());
        return attachmentMap;
    }


    @Override
    public SyncIssuesResult syncIssues(SyncIssuesRequest request){
//        MSPluginException.throwException("同步失败");
        //找出ms所需要的飞书字段模版
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS = null;
        platformCustomFieldItemDTOS = getThirdPartCustomField(request.getProjectConfig());
        //获取全部飞书issues
        LarkWorkItemRequest workItemRequest = new LarkWorkItemRequest(Arrays.asList("issue"));
        List<LarkWorkItemInfo> larkWorkItemInfos = larkAbstractClient.getWorkItemAll(workItemRequest);
        //婚礼纪个性化，用需求筛选项目
//        checkIssueByDemandId(larkWorkItemInfos, larkProjectConfig.getDemandId());
        //获取全部MSissues
        List<PlatformIssuesDTO> issues = request.getIssues();
        SyncIssuesResult syncIssuesResult = new SyncIssuesResult();
        //找出ms需要添加的缺陷
        syncIssuesResult.setAddIssues(getMSAddIssues(larkWorkItemInfos, issues, platformCustomFieldItemDTOS));
        //找出ms需要更新的缺陷
        syncIssuesResult.setUpdateIssues(getMSUpdateIssues(larkWorkItemInfos, issues, platformCustomFieldItemDTOS));
        //找出ms需要删除的缺陷
        syncIssuesResult.setDeleteIssuesIds(getMSDeleteIssues(larkWorkItemInfos, issues));
        //找出ms需要添加的附件 只获取添加或更新的附件
//        syncIssuesResult.setAttachmentMap(getMSAddAttachment(larkWorkItemInfos, syncIssuesResult));
        return syncIssuesResult;
    }

    public void checkIssueByDemandId(List<LarkWorkItemInfo> larkWorkItemInfos, String demandId){
        List<LarkWorkItemInfo> temp = new ArrayList<>();
        for(LarkWorkItemInfo item : larkWorkItemInfos){
            try{
                List<LarkFieldValuePairs> larkFieldValuePairs = item.getFields();
                if(larkFieldValuePairs == null){
                    temp.add(item);
                    continue;
                }
                for(LarkFieldValuePairs lfvp: larkFieldValuePairs){
                    try{
                        if(StringUtils.equals(lfvp.getField_type_key(), "work_item_related_select")){
                            if(!StringUtils.equals(String.valueOf(lfvp.getField_value()), demandId)){
                                temp.add(item);
                                break;
                            }
                        }
                    }catch (Exception e){
                        System.out.println("error item "+JSON.toJSONString(item)+" error lfvp "+JSON.toJSONString(lfvp));
                        temp.add(item);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
                temp.add(item);
                System.out.println("error json "+JSON.toJSONString(item));
                e.printStackTrace();
            }

        }
        for(LarkWorkItemInfo item : temp){
            larkWorkItemInfos.remove(item);
        }
    }

    @Override
    protected String getCustomFieldsValuesString(List<PlatformCustomFieldItemDTO> thirdPartCustomField) {
        List fields = new ArrayList();
        thirdPartCustomField.forEach((item) -> {
            Map<String, Object> field = new LinkedHashMap();
            field.put("customData", item.getCustomData());
            field.put("id", item.getId());
            field.put("name", item.getName());
            field.put("type", item.getType());
            String defaultValue = item.getDefaultValue();
            if (StringUtils.isNotBlank(defaultValue)) {
                try{
                    field.put("value", JSON.parseObject(defaultValue));
                }catch (Exception e){
                    field.put("value", defaultValue);
                }
            }
            fields.add(field);
        });
        return JSON.toJSONString(fields);
    }

    @Override
    public List<PlatformCustomFieldItemDTO> getThirdPartCustomField(String projectConfig){
        return getThirdPartCustomFieldIO(projectConfig);
//        LarkProjectConfig lpc = JSON.parseObject(projectConfig, LarkProjectConfig.class);
//        // json 可能等于三个值，null TIMEOUT object
//        String json = RedisSingleton.getInstance().getValue("getThirdPartCustomField", larkAbstractClient.PLUGIN_ID);
//        if(json == null){
//            List<PlatformCustomFieldItemDTO> temp = getThirdPartCustomFieldIO(projectConfig);
//            RedisSingleton.getInstance().setValue("getThirdPartCustomField", temp, larkAbstractClient.PLUGIN_ID);
//            return temp;
//        }
//
//        if(json.equals(RedisSingleton.TIMEOUT)){
//            ThreadPool tp = new ThreadPool(this, projectConfig);
//            tp.run();
//            LarkRedisPCFID larkRedisPCFID = RedisSingleton.getInstance().getLarkRedisPCFIDValue("getThirdPartCustomField", larkAbstractClient.PLUGIN_ID);
//            return larkRedisPCFID.getPlatformCustomFieldItemDTOList();
//        }
//
//        return JSON.parseArray(json, PlatformCustomFieldItemDTO.class);
    }

    public List<PlatformCustomFieldItemDTO> getThirdPartCustomFieldIO(String projectConfig){
        List<LarkFieldConf> larkSimpleFields = null;
        Map<String, LarkSimpleField> larkSimpleFieldMap = null;
        List<LarkUserInfo> larkUserInfos = null;
        List<DemandDTO> demandDTOS = new ArrayList<>();
        try {
            larkSimpleFields = larkAbstractClient.getThirdPartCustomField();
            larkSimpleFieldMap = larkAbstractClient.getSpaceField();
            larkUserInfos = larkAbstractClient.getTameUserInfoList();
            LarkProjectConfig lpc = JSON.parseObject(projectConfig, LarkProjectConfig.class);
            LarkWorkItemRequest larkWorkItemRequest = new LarkWorkItemRequest(Arrays.asList("story"));
            larkWorkItemRequest.setWork_item_ids(Arrays.asList(new Long[]{Long.valueOf(Long.parseLong(lpc.getDemandId()))}));
            List<LarkWorkItemInfo> larkWorkItemInfos = larkAbstractClient.getWorkItemAll(larkWorkItemRequest);
            for(LarkWorkItemInfo item : larkWorkItemInfos){
                if(StringUtils.equals(String.valueOf(item.getId()), lpc.getDemandId())){
                    demandDTOS.add(larkWorkItemToDemands(item));
                }
            }

//            demandDTOS = getDemands(projectConfig);

        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e.getMessage());
        }
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS = new ArrayList<>();
        List<MSOption> userList = new ArrayList<>();
        for(LarkUserInfo item: larkUserInfos){
            MSOption msOption = new MSOption();
            msOption.setText(item.getName_en());
            msOption.setValue(item.getUser_key());
            userList.add(msOption);
        }
        for(LarkFieldConf item : larkSimpleFields){
            //ms自带多个附件，跳过此字段
            if("multi_file".equals(item.getField_type_key()) || "file".equals(item.getField_type_key())){
                continue;
            }
            PlatformCustomFieldItemDTO temp = new PlatformCustomFieldItemDTO();
            settingValue(temp, item, larkSimpleFieldMap, userList, demandDTOS);
            platformCustomFieldItemDTOS.add(temp);
        }
        return platformCustomFieldItemDTOS;
    }

//    private List<MSOption> getJLXZ(List<LarkOption> ite){
//        List<MSOption> msOptionList = new ArrayList<>();
//        // 默认只可以选最底层
//        for(LarkOption item : ite){
//            if(item.getChildren() != null || item.getChildren().size() != 0){
//                recursionJLXZ(item.getValue());
//            }else{
//                msOptionList.add(new MSOption(item.getLabel(), item.getValue()));
//            }
//        }
//        return msOptionList;
//    }




    public void settingValue(PlatformCustomFieldItemDTO temp, LarkFieldConf item, Map<String, LarkSimpleField> larkSimpleFieldMap, List<MSOption> userList, List<DemandDTO> demandDTOS){
        //必填
        if(item.getIs_required() == 1) temp.setRequired(true);
        //默认值
        if(item.getDefault_value().getDefault_appear() == 1) temp.setDefaultValue(item.getDefault_value().getValue());
        //名称
        temp.setName(item.getField_name());
        //标示
        temp.setCustomData(item.getField_key());
        //类型
        temp.setType(FieldTypeMapping.getMsTypeBylarkType(item.getField_type_key()));
        if(StringUtils.equalsAny(item.getField_key(), "issue_reporter","issue_operator")){
            temp.setType(CustomFieldType.SELECT.getValue());
        };
        //值
        LarkSimpleField larkSimpleField = larkSimpleFieldMap.get(item.getField_key());
        //id
        temp.setId(item.getField_key());

        if(StringUtils.equals(item.getField_type_key(), "bool")){
            List<MSOption> msOptionList = new ArrayList<>();
            MSOption mTrue = new MSOption();
            MSOption mFalse = new MSOption();
            mTrue.setText("是");
            mTrue.setValue("true");
            msOptionList.add(mTrue);
            mFalse.setText("否");
            mFalse.setValue("false");
            msOptionList.add(mFalse);
            temp.setOptions(JSON.toJSONString(msOptionList));
        }else if(StringUtils.equals(item.getField_type_key(), "business")){
            LarkSimpleField larkSimpleField1 = larkSimpleFieldMap.get("business");

            // 默认只可以选最底层
//            List<MSOption> msOptionList = getJLXZ(larkSimpleField1.getOptions());
            List<MSOption> msOptionList = new ArrayList<>();
            for(LarkOption ite: larkSimpleField1.getOptions()){
                if(ite == null){
                    continue;
                } else if(ite.getChildren() == null){
                    MSOption msOption = new MSOption();
                    msOption.setText(ite.getLabel());
                    msOption.setValue(ite.getValue());
                    msOptionList.add(msOption);
                } else {
                    for(LarkOption it: ite.getChildren()){
                        MSOption msOption = new MSOption();
                        msOption.setText(ite.getLabel()+" / "+it.getLabel());
//                    msOption.setValue(ite.getValue()+"/"+it.getValue());
                        msOption.setValue(it.getValue());
                        msOptionList.add(msOption);
                    }
                }
            }
            temp.setOptions(JSON.toJSONString(msOptionList));
        }else if(StringUtils.equals(item.getField_type_key(), "tree_select")){
            List<MSOption> msOptionList = new ArrayList<>();
            for(LarkOptionConf ite: item.getOptions()){
                for(LarkOptionConf it: ite.getChildren()){
                    MSOption msOption = new MSOption();
                    msOption.setText(ite.getLabel()+" / "+it.getLabel());
                    msOption.setValue(ite.getValue()+"&"+it.getValue());
                    msOptionList.add(msOption);
                }
            }
            temp.setOptions(JSON.toJSONString(msOptionList));
        } else if(StringUtils.equals(item.getField_type_key(), "multi_user") ||
                StringUtils.equals( "user", item.getField_type_key())){
            temp.setOptions(JSON.toJSONString(userList));
        } else if(StringUtils.equals(item.getField_type_key(), "work_item_related_select")){
            List<MSOption> msOptionList = new ArrayList<>();
            for(DemandDTO ite : demandDTOS){
                MSOption mo = new MSOption();
                mo.setText(ite.getName());
                mo.setValue(ite.getId());
                msOptionList.add(mo);
            }
            temp.setOptions(JSON.toJSONString(msOptionList));
        } else {
            if(item.getOptions() != null && item.getOptions().size() != 0){
                temp.setOptions(item.getMsLarkOption());
            } else {
                temp.setOptions(larkSimpleField.getMsLarkOption());
            }
        }

    }

    @Override
    public ResponseEntity proxyForGet(String url, Class responseEntityClazz) {
        return larkAbstractClient.proxyForGet(url, responseEntityClazz);
    }

    @Override
    public void syncIssuesAttachment(SyncIssuesAttachmentRequest request) {
        larkAbstractClient.syncIssuesAttachment(request);
    }

    @Override
    public List<PlatformStatusDTO> getStatusList(String issueKey) {
        Map<String, LarkSimpleField> larkSimpleFieldMap = larkAbstractClient.getSpaceField();
        LarkSimpleField larkSimpleField = larkSimpleFieldMap.get("work_item_status");
        List<PlatformStatusDTO> l = new ArrayList<>();
        for(LarkOption item:larkSimpleField.getOptions()){
            if(StringUtils.equals("issue", item.getWork_item_type_key())){
                PlatformStatusDTO p = new PlatformStatusDTO();
                p.setValue(item.getValue());
                p.setLabel(item.getLabel());
                l.add(p);
            }
        }
        return l;
    }

    @Override
    public void syncAllIssues(SyncAllIssuesRequest syncRequest){
        List<Map> zentaoIssues = new ArrayList<>();
        LarkProjectConfig larkProjectConfig = JSON.parseObject(syncRequest.getProjectConfig(), LarkProjectConfig.class);
        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOS = getThirdPartCustomField(syncRequest.getProjectConfig());
//        ThreadPool tp = new ThreadPool(this, syncRequest.getProjectConfig());
//        tp.run();
        //获取全部飞书issues
//        LarkWorkItemRequest workItemRequest = new LarkWorkItemRequest(Arrays.asList("issue"));

        System.out.println("start get item project all issus");
//        List<LarkWorkItemInfo> larkWorkItemInfos = larkAbstractClient.getWorkItemAll(workItemRequest);
        List<LarkWorkItemInfo> larkWorkItemInfos = larkAbstractClient.searchWorkItemAll(getLarkSWI(larkProjectConfig.getDemandId()), "issue");
//        checkIssueByDemandId(larkWorkItemInfos, larkProjectConfig.getDemandId());
        System.out.println("get work Item size:"+larkWorkItemInfos.size());
        SyncAllIssuesResult syncIssuesResult = new SyncAllIssuesResult();
        //找出ms需要添加的缺陷
        List<IssuesWithBLOBs> issuesWithBLOBsList = getMSAddIssues(larkWorkItemInfos, null, platformCustomFieldItemDTOS);
        syncIssuesResult.setAddIssues(issuesWithBLOBsList);
        System.out.println("get work Item size:"+syncIssuesResult.getAddIssues().size());
        this.defaultCustomFields = syncRequest.getDefaultCustomFields();
        for(IssuesWithBLOBs item : syncIssuesResult.getAddIssues()){
            zentaoIssues.add(JSON.parseMap(JSON.toJSONString(item)));
        }
        try {
            List<String> allIds = zentaoIssues.stream().map(i -> i.get("id").toString()).collect(Collectors.toList());
            syncIssuesResult.setAllIds(allIds);
            if (syncRequest != null) {
                zentaoIssues = filterSyncZentaoIssuesByCreated(zentaoIssues, syncRequest);
            }
            syncIssuesResult.setUpdateIssues(getMSUpdateIssues(larkWorkItemInfos, null, platformCustomFieldItemDTOS));
            System.out.println("get work Item size:"+syncIssuesResult.getUpdateIssues().size());
            syncIssuesResult.getUpdateIssues().addAll(syncIssuesResult.getAddIssues());
            HashMap<Object, Object> syncParam = buildSyncAllParam(syncIssuesResult);
            Consumer<Map> hsf = syncRequest.getHandleSyncFunc();
            hsf.accept(syncParam);
            System.out.println("syncAllIssues dome add Issues size:"+syncIssuesResult.getAddIssues().size()
            +" update Issues size:"+syncIssuesResult.getUpdateIssues().size()
            +" all ids size:"+syncIssuesResult.getAllIds().size()
            +" del issues size:"+syncIssuesResult.getDeleteIssuesIds().size());
        } catch (Exception e) {
            LogUtil.error(e);
            MSPluginException.throwException(e);
        }
    }

    public LarkSearchWorkItemRequest getLarkSWI(String dId){
        LarkSearchWorkItemRequest larkWorkItemRequest = new LarkSearchWorkItemRequest();
        larkWorkItemRequest.setPage_num(1l);
        LarkSearchGroup larkSearchGroup = new LarkSearchGroup();
        larkSearchGroup.setConjunction("AND");
        LarkSearchParam larkSearchParam = new LarkSearchParam();
        larkSearchParam.setOperator("HAS ANY OF");
        larkSearchParam.setParam_key("_field_linked_story");
        larkSearchParam.setValue(Arrays.asList(Long.parseLong(dId)));
        larkSearchGroup.getSearch_params().add(larkSearchParam);
        larkWorkItemRequest.setSearch_group(larkSearchGroup);
        return larkWorkItemRequest;
    }

    public List<Map> filterSyncZentaoIssuesByCreated(List<Map> zentaoIssues, SyncAllIssuesRequest syncRequest) {
        List<Map> filterIssues = zentaoIssues.stream().filter(item -> {
            if(syncRequest.getCreateTime() == null){
                return true;
            } else {
                long createTimeMills = 0;
                try {
                    createTimeMills = Long.parseLong(String.valueOf(item.get("createTime")));
                    if (syncRequest.isPre()) {
                        return createTimeMills <= syncRequest.getCreateTime().longValue();
                    } else {
                        return createTimeMills >= syncRequest.getCreateTime().longValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }).collect(Collectors.toList());
        return filterIssues;
    }
}
