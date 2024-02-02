package io.metersphere.platform.commons;

import io.metersphere.platform.constants.CustomFieldType;

public enum FieldTypeMapping {
    //只记录需要特殊处理的
    MULTI_TEXT(CustomFieldType.RICH_TEXT.getValue(),"multi_text"),
    TEXT(CustomFieldType.INPUT.getValue(),"text"),
    WORK_ITEM_TEMPLATE(CustomFieldType.SELECT.getValue(),"work_item_template"),
    MULTI_SELECT(CustomFieldType.MULTIPLE_SELECT.getValue(),"multi_select"),
    MULTI_USER(CustomFieldType.MULTIPLE_SELECT.getValue(),"multi_user"),
    TREE_SELECT(CustomFieldType.SELECT.getValue(),"tree_select"),
    BUSINESS(CustomFieldType.SELECT.getValue(),"business"),
    BOOL(CustomFieldType.SELECT.getValue(),"bool"),
    USER(CustomFieldType.SELECT.getValue(),"user"),
    WORK_ITEM_RELATED_SELECT(CustomFieldType.SELECT.getValue(),"work_item_related_select"),
    VOTE_OPTION_MULTI(CustomFieldType.MULTIPLE_SELECT.getValue(),"vote_option_multi");

    private String msType;
    private String larkType;

    FieldTypeMapping(String msType , String larkType) {
        this.msType = msType;
        this.larkType = larkType;
    }

    public static String getMsTypeBylarkType(String type){
        for(FieldTypeMapping item : FieldTypeMapping.values()){
            if(item.larkType.equals(type)){
                return item.msType;
            }
        }
        return type;
    }
}
