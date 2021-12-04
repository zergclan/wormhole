package com.zergclan.wormhole.common.data;

import com.zergclan.wormhole.common.data.test.DemoTestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class DataGroupTest {

    @Test
    public void assertNewInstance() {
        DataGroup dataGroup = new DemoTestData(1L, 2L, 3L);
        assertNotNull(dataGroup);
    }
}
