package io.metersphere.platform.impl;

import io.metersphere.platform.api.AbstractPlatformMetaInfo;

public class LarkPlatformMetaInfo extends AbstractPlatformMetaInfo {

    public static final String KEY = "Lark";

    public LarkPlatformMetaInfo() {
        super(LarkPlatformMetaInfo.class.getClassLoader());
    }

    @Override
    public String getVersion() {
        return "2.10.6";
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public boolean isThirdPartTemplateSupport() {
        return true;
    }
}
