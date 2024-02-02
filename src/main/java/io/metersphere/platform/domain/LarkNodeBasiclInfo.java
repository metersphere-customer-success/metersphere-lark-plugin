package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkNodeBasiclInfo {
    private String id;
    private String name;
    private List<String> owners;
}
