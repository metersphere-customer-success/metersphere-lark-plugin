package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LarkConnections {
    private String source_state_key;
    private String target_state_key;
    private Integer transition_id;
}
