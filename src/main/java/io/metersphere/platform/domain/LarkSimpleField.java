package io.metersphere.platform.domain;

import io.metersphere.plugin.utils.JSON;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LarkSimpleField {
    private String field_key;
    private String field_alias;
    private String field_type_key;
    private String field_name;
    private String is_custom_field;
    private String is_obsoleted;
    private List<LarkOption> options;
    private List<LarkSimpleField> compound_fields;

    public String getMsLarkOption() {
        if(this.options == null || this.options.size() == 0){
            return null;
        }
        List<MSOption> temp = new ArrayList<>();
        for(LarkOption item : this.options){
            MSOption larkOption = new MSOption();
//            larkOption.setChildren(item.getChildren());
            larkOption.setText(item.getLabel());
            larkOption.setValue(item.getValue());
            temp.add(larkOption);
        }
        return JSON.toJSONString(temp);
    }
}
