package io.metersphere.platform.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MSOption {
    private String text;
    private Object value;

    public MSOption(String label, Object value) {
        this.text = label;
        this.value = value;
    }

    public MSOption(){}
}
