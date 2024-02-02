package io.metersphere.platform.domain;

import io.metersphere.plugin.utils.JSON;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LarkResponseBase {
    private LarResponseError error;
    private Object data;
    private int err_code;
    private String err_msg;

    public String getDataStr() {
        return JSON.toJSONString(this.data);
    }
}
@Getter
@Setter
class LarResponseError {
    private int code;
    private String msg;
    private DisplayMsg display_msg;
}
@Getter
@Setter
class DisplayMsg {
    private String title;
    private String content;
}
