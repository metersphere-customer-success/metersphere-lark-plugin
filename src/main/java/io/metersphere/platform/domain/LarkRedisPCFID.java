package io.metersphere.platform.domain;

import lombok.Data;

import java.util.List;

@Data
public class LarkRedisPCFID {
    private long time;
    private List<PlatformCustomFieldItemDTO> platformCustomFieldItemDTOList;
}
