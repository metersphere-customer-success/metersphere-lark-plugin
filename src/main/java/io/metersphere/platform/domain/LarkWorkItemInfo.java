package io.metersphere.platform.domain;

import io.metersphere.base.domain.IssuesWithBLOBs;
import io.metersphere.plugin.utils.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class LarkWorkItemInfo {
    private long id;
    private String name;
    private String work_item_type_key;
    private String project_key;
    private long template_id;
    private String template_type;
    private String pattern;
    private String sub_stage;
    private List<LarkNodeBasiclInfo> current_nodes;
    private String created_by;
    private String updated_by;
    private String deleted_by;
    private long created_at;
    private long updated_at;
    private long deleted_at;
    private List<LarkFieldValuePairs> fields;
    private LarkWorkItemStatus work_item_status;
//    private LarkNworkflow_infos
    private List<LarkStateTime> state_times;

    public String getMSId(){
        return this.project_key + "_" + this.id;
    }
//    public IssuesWithBLOBs toIssuesWithBLOB(String userKey, Map<String, LarkSimpleField> larkSimpleFieldMap) {
//        IssuesWithBLOBs issues = new IssuesWithBLOBs();
//        issues.setId(getMSId());
//        issues.setPlatformId(getMSId());
//        issues.setPlatform("Lark");
//        issues.setUpdateTime(updated_at);
//        issues.setCreateTime(created_at);
//        HashMap<String, LarkFieldValuePairs> map = new HashMap<>();
//        for(LarkFieldValuePairs item : fields){
//            map.put(item.getField_key(), item);
//        }
//        List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOs = new ArrayList<>();
//        for(LarkFieldValuePairs item : fields){
//            LarkSimpleField larkSimpleField = larkSimpleFieldMap.get(item.getField_key());
//            if(larkSimpleField == null){
//                //需做特殊处理的字段
//                continue;
//            }else{
//                PlatformCustomFieldItemDTO platformCustomFieldItemDTO = new PlatformCustomFieldItemDTO();
//                platformCustomFieldItemDTO.setCustomData(item.getField_key());
//                platformCustomFieldItemDTO.setName(larkSimpleField.getField_name());
//                platformCustomFieldItemDTO.setType(FieldTypeMapping.getMsTypeBylarkType(item.getField_type_key()));
//                setValue(item, platformCustomFieldItemDTO, userKey);
//                platformCustomFieldItemDTOs.add(platformCustomFieldItemDTO);
//            }
//        }
//        issues.setCustomFields(JSON.toJSONString(platformCustomFieldItemDTOs));
//        return issues;
//    }
    public IssuesWithBLOBs toIssuesWithBLOB(String userKey, List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOs) {
        IssuesWithBLOBs issues = new IssuesWithBLOBs();
        issues.setId(getMSId());
        issues.setPlatformId(getMSId());
        issues.setPlatform("Lark");
        issues.setPlatformStatus(sub_stage);
        issues.setUpdateTime(updated_at);
        issues.setCreateTime(created_at);
        HashMap<String, LarkFieldValuePairs> map = new HashMap<>();
        for(LarkFieldValuePairs item : fields){
            map.put(item.getField_key(), item);
        }
        for(PlatformCustomFieldItemDTO p : platformCustomFieldItemDTOs){
            // 从模版往缺陷里添加值
            LarkFieldValuePairs larkFieldValuePairs = map.get(p.getCustomData());
            if(larkFieldValuePairs == null){
                switch (p.getCustomData()) {
                    case "updated_at":
                        Date date = new Date(this.updated_at);
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                        try{
                            String dateStr = s.format(date);
                            p.setValue(dateStr);
                        } catch (Exception e){
                        }
                        break;
                    case "name":
                        p.setValue(this.name);
                        issues.setTitle(this.name);
                        break;
                }
                continue;
            }
            if(larkFieldValuePairs.getField_value() == null ||
                    StringUtils.equals("",larkFieldValuePairs.getField_value()+"") ||
                    StringUtils.equals("[]",larkFieldValuePairs.getField_value()+"")){
                p.setValue(null);
            } else {
                setValue(larkFieldValuePairs, p, userKey, issues);
            }

//            PlatformCustomFieldItemDTO platformCustomFieldItemDTO = new PlatformCustomFieldItemDTO();
//            platformCustomFieldItemDTO.setCustomData(item.getField_key());
//            platformCustomFieldItemDTO.setType(FieldTypeMapping.getMsTypeBylarkType(item.getField_type_key()));
//            platformCustomFieldItemDTO.setName();
//            platformCustomFieldItemDTO.setId(item.getField_key());
//            platformCustomFieldItemDTOs.add(platformCustomFieldItemDTO);
        }
        issues.setCustomFields(JSON.toJSONString(platformCustomFieldItemDTOs));
        return issues;
    }

    private void setValue(LarkFieldValuePairs larkFieldValuePairs, PlatformCustomFieldItemDTO item , String userKey, IssuesWithBLOBs issues){
        switch (larkFieldValuePairs.getField_type_key()){
            case "bool":
                item.setValue(larkFieldValuePairs.getField_value());
                break;
            case "business":
                item.setValue((larkFieldValuePairs.getField_value()+"").replace("\"",""));
//                Map<String, Object> businessMap = JSON.parseMap(JSON.toJSONString(larkFieldValuePairs.getField_value()));
//                Map<String, String> businessMapSub = JSON.parseMap(JSON.toJSONString(businessMap.get("children")));
//                if(businessMap != null && businessMapSub != null){
//                    item.setValue(businessMap.get("value")+"&"+businessMapSub.get("value"));
//                } else if(businessMap != null){
//                    item.setValue(businessMap.get("value"));
//                } else if(businessMapSub != null){
//                    item.setValue(businessMapSub.get("value"));
//                } else {
//                    System.out.println("error fieldValuePairs " + JSON.toJSONString(larkFieldValuePairs));
//                }
                break;
            case "tree_select":
                Map<String, Object> map = JSON.parseMap(JSON.toJSONString(larkFieldValuePairs.getField_value()));
                Map<String, String> mapSub = JSON.parseMap(JSON.toJSONString(map.get("children")));
                if(map != null && mapSub != null){
                    item.setValue(map.get("value")+"&"+mapSub.get("value"));
                } else if(map != null){
                    item.setValue(map.get("value"));
                } else if(mapSub != null){
                    item.setValue(mapSub.get("value"));
                } else {
                    System.out.println("error fieldValuePairs " + JSON.toJSONString(larkFieldValuePairs));
                }
                break;
            case "date":
                Date date = new Date(Long.parseLong(larkFieldValuePairs.getField_value()+""));
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    String dateStr = s.format(date);
                    item.setValue(dateStr);
                } catch (Exception e){

                }
                break;
            case "vote_option_multi":
                Map<String,Object> tempMap = JSON.parseMap(JSON.toJSONString(larkFieldValuePairs.getField_value()));
                Object info = tempMap.get(userKey);
                @Getter
                @Setter
                class TempClass{private String key,tip="";}
                List<TempClass> list = JSON.parseArray(JSON.toJSONString(info),TempClass.class);
                List<String> values = new ArrayList<>();
                for(TempClass i : list){
                    values.add(i.getKey());
                }
                item.setValue(values);
                break;
            case "select":
                Map<String,Object> mapSelect = JSON.parseMap(JSON.toJSONString(larkFieldValuePairs.getField_value()));
                item.setValue(mapSelect.get("value"));
                break;
            case "multi_select":
                List<String> strings = new ArrayList<>();
                List tempList = JSON.parseArray(JSON.toJSONString(larkFieldValuePairs.getField_value()));
                for(int i = 0 ; i < tempList.size(); i++){
                    String temp = JSON.toJSONString(tempList.get(i));
                    Map<String, String> m = JSON.parseMap(temp);
                    strings.add(m.get("value"));
                }
                item.setValue(strings);
                break;
            case "work_item_template":
                String str = JSON.toJSONString(larkFieldValuePairs.getField_value());
                Map<String, Integer> templateMap = JSON.parseMap(str);
                item.setValue(templateMap.get("id")+"");
                break;
            case "work_item_related_select":
                item.setValue(getProject_key()+"_"+larkFieldValuePairs.getField_value()+"");
                break;
            case "multi_user":
                // 飞书bug，实际是单选，给的是多选
                if(StringUtils.equals(larkFieldValuePairs.getField_key(), "issue_operator") ||
                StringUtils.equals(larkFieldValuePairs.getField_key(), "issue_reporter")){
                    List<String> strList = JSON.parseArray(JSON.toJSONString(larkFieldValuePairs.getField_value()));
                    item.setValue(strList.get(0));
                } else {
                    item.setValue(larkFieldValuePairs.getField_value());
                }
                break;
            default:
                if(StringUtils.equals(item.getCustomData(), "description")){
                    issues.setDescription(larkFieldValuePairs.getField_value() + "");
                }
                item.setValue(larkFieldValuePairs.getField_value());
        }
    }
}
