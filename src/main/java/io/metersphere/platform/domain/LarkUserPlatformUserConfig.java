package io.metersphere.platform.domain;

import lombok.Data;

@Data
public class LarkUserPlatformUserConfig {
    private String pluginSecret;
    private String pluginId;
    private String userKey;

    public void setLarkConfig(LarkConfig larkConfig){

    }
}
