package io.metersphere.platform.domain;

import com.alibaba.fastjson2.JSONArray;
import io.metersphere.plugin.utils.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class LarkAddWorkItem {
    private String work_item_type_key;
    private String name;
    private List<LarkFieldValuePairs> field_value_pairs = new ArrayList<>();
    private Integer template_id;

    public LarkAddWorkItem(PlatformIssuesUpdateRequest data, Map<String, LarkFieldConf> larkSimpleFieldMap, String itemStype, String userKey) {
        this.work_item_type_key = itemStype;
        for(PlatformCustomFieldItemDTO item : data.getCustomFieldList()){
            LarkFieldValuePairs larkFieldValuePairs = new LarkFieldValuePairs();
            String larkType = null;
            LarkFieldConf larkSimpleField = larkSimpleFieldMap.get(item.getCustomData());

            if(larkSimpleField != null && larkSimpleField.getField_type_key() != null && !("").equals(larkSimpleField.getField_type_key())){
                larkType = larkSimpleField.getField_type_key();
            } else {
                larkType = item.getType();
            }
            // 做个空字符串判断
            if(StringUtils.equals("",item.getValue()+"")){
                item.setValue(null);
            } else {
                setdata(larkFieldValuePairs, larkType, item, userKey, data);
                //跳过某些写在外面的类型
                if(("work_item_template").equals(larkType)){
                    this.template_id = Integer.parseInt(item.getValue()+"");
                    continue;
                }
                field_value_pairs.add(larkFieldValuePairs);
            }
        }

//        this.name = data.getTitle();
//        // 固定把描述先添加进去
//        LarkFieldValuePairs temp = new LarkFieldValuePairs();
//        temp.setField_key("description");
//        temp.setField_value(data.getDescription());
//        field_value_pairs.add(temp);
//
//        for(PlatformCustomFieldItemDTO item : data.getCustomFieldList()){
//            LarkFieldValuePairs larkFieldValuePairs = new LarkFieldValuePairs();
//            setdata(larkFieldValuePairs , item);
//            field_value_pairs.add(larkFieldValuePairs);
//        }
    }

    private void setdata(LarkFieldValuePairs larkFieldValuePairs, String larkType, PlatformCustomFieldItemDTO item , String userKey, PlatformIssuesUpdateRequest data){
        larkFieldValuePairs.setField_key(item.getCustomData());
        larkFieldValuePairs.setField_type_key(larkType);
        String[] key = null;
        switch (larkType){
            case "bool":
                if(StringUtils.equals("true", item.getValue()+"")){
                    larkFieldValuePairs.setField_value(true);
                } else if(StringUtils.equals("false", item.getValue()+"")){
                    larkFieldValuePairs.setField_value(false);
                } else {
                    larkFieldValuePairs.setField_value(item.getValue());
                }
                break;
            case "business":
//                String businessValue = item.getValue()+"";
//                key = businessValue.split("/");
//                @Data
//                class BusinessChildren{
//                    private String value;
//                    private BusinessChildren children;
//                }
//                BusinessChildren businessChildren = new BusinessChildren();
//                businessChildren.setValue(key[0]);
//                BusinessChildren businessChildrenSub = new BusinessChildren();
//                businessChildren.setChildren(businessChildrenSub);
//                businessChildrenSub.setValue(key[1]);
//                larkFieldValuePairs.setField_value(businessChildren);
//                larkFieldValuePairs.setField_value((item.getValue()+"").replace("\"",""));
//                larkFieldValuePairs.setField_name(item.getName());
                larkFieldValuePairs.setField_value(item.getValue()+"");
//                @Getter
//                @Setter
//                class BusinessSelectValue{private Object value;}
//                BusinessSelectValue businessv = new BusinessSelectValue();
//                businessv.setValue(item.getValue());
//                larkFieldValuePairs.setField_value(businessv);
                break;
            case "tree_select":
                String value = item.getValue()+"";
                key = value.split("&");
                @Data
                class Children{
                    private String value;
                    private Children children;
                }
                Children children = new Children();
                children.setValue(key[0]);
                Children childrenSub = new Children();
                children.setChildren(childrenSub);
                childrenSub.setValue(key[1]);
                larkFieldValuePairs.setField_value(children);
                break;
            case "date":
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    Date d = s.parse(item.getValue() + "");
                    larkFieldValuePairs.setField_value(d.getTime());
                } catch (Exception e){

                }
                break;
            case "vote_option_multi":
                Map<String,Object> tempMap = new HashMap<>();
                List<Object> list = new ArrayList<>();
                List<String> valueList = JSON.parseArray(item.getValue()+"",String.class);
                @Getter
                @Setter
                class TempClass{private String key,tip="";}
                for(String str : valueList){
                    TempClass t = new TempClass();
                    t.setKey(str);
                    list.add(t);
                }
                tempMap.put(userKey,list);
                tempMap.put("TOTALVOTESCORE",0);
//                String value = JSON.toJSONString();
                larkFieldValuePairs.setField_value(tempMap);
                break;
            case "select":
                @Getter
                @Setter
                class SelectValue{private Object value;}
                SelectValue v = new SelectValue();
                v.setValue(item.getValue());
                larkFieldValuePairs.setField_value(v);
                break;
            case "multi_select":
                @Getter
                @Setter
                class MultiSelectValue{private Object value;}
                List<MultiSelectValue> multiSelectList = new ArrayList<>();
                List<String> multiSelectValueList = JSON.parseArray(item.getValue()+"",String.class);
                for(String str : multiSelectValueList){
                    MultiSelectValue multiSelectValue = new MultiSelectValue();
                    multiSelectValue.setValue(str);
                    multiSelectList.add(multiSelectValue);
                }
                larkFieldValuePairs.setField_value(multiSelectList);
                break;
            case "multi_user":
                List<String> temp = new ArrayList<>();
                //飞书的bug特殊处理一下
                if(StringUtils.equals("issue_operator", item.getCustomData()) || StringUtils.equals("issue_reporter", item.getCustomData())){
                    temp.add(item.getValue()+"");
                    larkFieldValuePairs.setField_value(temp);
                    break;
                }
                temp = JSON.parseArray(item.getValue()+"");
                larkFieldValuePairs.setField_value(temp);
                break;
            case "work_item_related_select":
                String msid = item.getValue()+"";
                key = msid.split("_");
                larkFieldValuePairs.setField_value(Integer.parseInt(key[1]));
                break;
            default:
                larkFieldValuePairs.setField_value(item.getValue());
                if(StringUtils.equals(item.getCustomData(), "name")){
                    data.setTitle(item.getValue()+"");
//                    this.name = item.getValue()+"";
                } else if(StringUtils.equals(item.getCustomData(), "description")){
                    data.setDescription(item.getValue()+"");
                }
        }
    }

//    private void setdata(LarkFieldValuePairs larkFieldValuePairs, PlatformCustomFieldItemDTO item){
//        larkFieldValuePairs.setField_key(item.getCustomData());
//        String data = item.getValue() + "";
//        if(StringUtils.isEmpty(data)){
//            MSPluginException.throwException("请检查插件所使用的模版");
//        }
//        String[] value = data.split(",");
//        String type = value[0];
//        switch (type){
//            case "select":
//                String key = value[1];
//                String label = value[2];
//                LarkOption s = new LarkOption();
//                s.setLabel(label);
//                s.setValue(key);
//                larkFieldValuePairs.setField_value(s);
//            default:MSPluginException.throwException("请检查插件所使用的模版");
//        }
//    }
}
