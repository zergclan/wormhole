package com.zergclan.wormhole.web.application.domain.value;

import lombok.Getter;

@Getter
public enum LoginType {
    
    USERNAME(0);
    
    private final Integer code;
    
    LoginType(final Integer code) {
        this.code = code;
    }
}
