package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LarkWorkItemRequest {
    private String work_item_name;
    private List<String> user_keys;
    private List<String> work_item_type_keys;
    private List<Long> work_item_ids;
    private LarkTimeInterval created_at;
    private LarkTimeInterval updated_at;
    private List<String> sub_stages;
    private List<LarkWorkItemStatus> work_item_status;
    private int page_size;
    private int page_num;

    public LarkWorkItemRequest(List<String> work_item_type_keys) {
        this.work_item_type_keys = work_item_type_keys;
    }

    private LarkWorkItemRequest() {}
}
