package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkUpdateWorkItemRequest {
    private List<LarkFieldValuePairs> update_fields;
}
