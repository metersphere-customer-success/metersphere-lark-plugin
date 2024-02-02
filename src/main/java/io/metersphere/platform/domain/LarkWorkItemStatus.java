package io.metersphere.platform.domain;

import java.util.List;

public class LarkWorkItemStatus {
    private String state_key;
    private boolean is_archived_state;
    private boolean is_init_state;
    private long updated_at;
    private String updated_by;
    private List<LarkWorkItemStatus> history;
}
