package com.zergclan.wormhole.web.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultCodeTest {

    @Test
    public void assertNew() {
        assertEquals(100000, ResultCode.SUCCESS.getCode());
        assertEquals(500000, ResultCode.FAILED.getCode());
        assertEquals(400000, ResultCode.BAD_REQUEST.getCode());
        assertEquals(999999, ResultCode.ERROR.getCode());
        assertEquals("SUCCESS", ResultCode.SUCCESS.getMessage());
        assertEquals("FAILED", ResultCode.FAILED.getMessage());
        assertEquals("BAD_REQUEST", ResultCode.BAD_REQUEST.getMessage());
        assertEquals("ERROR", ResultCode.ERROR.getMessage());
    }
}
