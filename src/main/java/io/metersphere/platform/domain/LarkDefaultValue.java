package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LarkDefaultValue {
    private int default_appear;
//    Appear     = 1 //默认出现
//    NoAppear   = 2 //默认不出现
//    CondAppear = 3 //条件出现

    private String value;
}
