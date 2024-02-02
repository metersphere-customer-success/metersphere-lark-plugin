package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LarkStateTime {
    private String state_key;
    private long start_time;
    private long end_time;
    private String name;

}
