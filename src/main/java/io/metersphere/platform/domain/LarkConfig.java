package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LarkConfig {
    private String pluginId;
    private String pluginSecret;
    private String url;
    private String userKey;
    private String spaceId;
}
