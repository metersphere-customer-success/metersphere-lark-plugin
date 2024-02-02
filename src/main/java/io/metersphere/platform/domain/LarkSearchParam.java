package io.metersphere.platform.domain;

import lombok.Data;

@Data
public class LarkSearchParam {
    private String param_key;
    private Object value;
    private String operator;
}
