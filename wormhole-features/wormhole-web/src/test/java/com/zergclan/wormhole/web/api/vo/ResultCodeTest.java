package com.zergclan.wormhole.web.api.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultCodeTest {

    @Test
    public void assertNew() {
        assertEquals(200, ResultCode.SUCCESS.getCode());
        assertEquals(500, ResultCode.FAILED.getCode());
        assertEquals(400, ResultCode.BAD_REQUEST.getCode());
        assertEquals(401, ResultCode.UNAUTHORIZED.getCode());
        assertEquals(999, ResultCode.ERROR.getCode());
        assertEquals("SUCCESS", ResultCode.SUCCESS.getMessage());
        assertEquals("FAILED", ResultCode.FAILED.getMessage());
        assertEquals("BAD_REQUEST", ResultCode.BAD_REQUEST.getMessage());
        assertEquals("UNAUTHORIZED", ResultCode.UNAUTHORIZED.getMessage());
        assertEquals("ERROR", ResultCode.ERROR.getMessage());
    }
}
