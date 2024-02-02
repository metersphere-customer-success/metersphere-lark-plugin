package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkOptionConf {
    private String label;
    private String value;
    private int is_visibility;
    private int is_disabled;
    private List<LarkOptionConf> children;
}
