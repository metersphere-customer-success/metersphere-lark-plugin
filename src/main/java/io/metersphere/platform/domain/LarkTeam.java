package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkTeam {
    private Integer team_id;
    private String team_name;
    private List<String> user_keys;
    private List<String> administrators;
}
