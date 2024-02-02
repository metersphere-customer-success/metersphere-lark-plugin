package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkSearchWorkItemRequest {
    private LarkSearchGroup search_group;
    private long page_size = 50l;//max 50
    private long page_num;
    private LarkExPand expand;

}
