package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LarkProjectConfig {
    private String larkIssueTypeId;
    private String larkStoryTypeId;
    private boolean thirdPartTemplate;
    private String demandId;
}
