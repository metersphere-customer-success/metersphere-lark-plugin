package io.metersphere.platform.singleton;

import io.metersphere.platform.domain.PlatformCustomFieldItemDTO;
import io.metersphere.platform.impl.LarkPlatform;

import java.util.List;

public class ThreadPool extends Thread {

    private LarkPlatform larkPlatform;
    private String projectConfig;

    public ThreadPool (LarkPlatform larkPlatform, String projectConfig){
        this.larkPlatform = larkPlatform;
        this.projectConfig = projectConfig;
    }

//    @Override
//    public void run() {
//        List<PlatformCustomFieldItemDTO> temp = larkPlatform.getThirdPartCustomFieldIO(projectConfig);
//        RedisSingleton.getInstance().setValue("getThirdPartCustomField", temp, larkPlatform.larkAbstractClient.PLUGIN_ID);
//    }
}
