package io.metersphere.platform.commons;

import org.springframework.http.HttpMethod;

public enum URLEnum {
    PLUGIN_TOKEN("/bff/v2/authen/plugin_token", HttpMethod.POST),
    PROJECTS("/open_api/projects", HttpMethod.POST),
    USER("/open_api/user/query", HttpMethod.POST),
    ADD_ISSUE("/open_api/%s/work_item/create", HttpMethod.POST),
    ISSUETYPES("/open_api/%s/work_item/all-types", HttpMethod.GET),
    PROJECTS_DETAIL("/open_api/projects/detail", HttpMethod.POST),
    GET_TEMPLATE("/open_api/%s/work_item/issue/meta", HttpMethod.GET),
    GET_WORK_ITEM("/open_api/%s/work_item/filter", HttpMethod.POST),
    SEARCH_WORK_ITEM("/open_api/%s/work_item/%s/search/params", HttpMethod.POST),
    DELETE_WORK_ITEM("/open_api/%s/work_item/%s/%s", HttpMethod.DELETE),
    UPLOAD_FILE("/open_api/%s/work_item/%s/%s/file/upload", HttpMethod.POST),
    UPDATE_WORK_ITEM("/open_api/%s/work_item/%s/%s", HttpMethod.PUT),

    WORKFLOW_QUERY("/open_api/%s/work_item/%s/%s/workflow/query", HttpMethod.POST),

    NODE_STATE_CHANGE("/open_api/%s/workflow/%s/%s/node/state_change", HttpMethod.POST),

    TEAMS_ALL("/open_api/%s/teams/all", HttpMethod.GET),
    GET_SPACE_FIELD("/open_api/%s/field/all", HttpMethod.POST);


    private String url;
    private HttpMethod httpMethod;

    URLEnum(String url , HttpMethod httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl(String... pathValue) {
        return String.format(url, pathValue);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
