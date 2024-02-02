package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LarkWorkFlow {
    private List<LarkConnections> connections;
}
