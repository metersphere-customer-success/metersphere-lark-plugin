package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LarkFieldValuePairs {
    private String field_key;
//    private String field_alias;
    private Object field_value;
    private String field_type_key;
    private String field_name;
//    private String target_state

}
