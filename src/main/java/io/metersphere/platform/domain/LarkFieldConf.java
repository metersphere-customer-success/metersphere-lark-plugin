package io.metersphere.platform.domain;

import io.metersphere.plugin.utils.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LarkFieldConf {
    private int is_required;
    private int is_visibility;
    private int is_validity;
//            role_assign;
    private String field_name;
    private String field_key;
    private String field_type_key;
    private LarkDefaultValue default_value;
    private String label;
    private List<LarkOptionConf> options;
    private List<LarkFieldConf> compound_fields;

    public String getMsLarkOption() {
        if(this.options == null || this.options.size() == 0){
            return null;
        }
        List<MSOption> temp = new ArrayList<>();
        for(LarkOptionConf item : this.options){
            MSOption larkOption = new MSOption();
//            larkOption.setChildren(item.getChildren());
            larkOption.setText(item.getLabel());
            larkOption.setValue(item.getValue());
            temp.add(larkOption);
        }
        return JSON.toJSONString(temp);
    }

}
