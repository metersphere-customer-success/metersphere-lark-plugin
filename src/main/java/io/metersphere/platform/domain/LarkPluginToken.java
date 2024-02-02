package io.metersphere.platform.domain;

public class LarkPluginToken extends LarkResponseBase{
    private String token;
    private Long expire_time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }
}
