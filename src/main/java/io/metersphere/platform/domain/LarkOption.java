package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkOption {
    private String label;
    private String value;
    private List<LarkOption> children;
    private String work_item_type_key;
}
