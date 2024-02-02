package io.metersphere.platform.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LarkSearchGroup {

    private List<LarkSearchParam> search_params = new ArrayList<>();
    private String conjunction;
    private List<LarkSearchGroup>search_groups = new ArrayList<>();

}
