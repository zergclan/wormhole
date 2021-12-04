package com.zergclan.wormhole.common.data;

import com.zergclan.wormhole.common.data.type.DatePattern;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class DataNodeTest {

    @Test
    public void assertStringDataNode() {
        DataNode<String> stringDataNode = new StringDataNode("column");
        assertNotNull(stringDataNode);
        stringDataNode.setValue("value");
        assertEquals("value", stringDataNode.getValue());
    }

    @Test
    public void assertIntegerDataNode() {
        DataNode<Integer> integerDataNode = new IntegerDataNode("column");
        assertNotNull(integerDataNode);
        integerDataNode.setValue(1);
        assertEquals(1, integerDataNode.getValue());
    }

    @Test
    public void assertDateDataNode() {
        DataNode<Date> dateDataNode = new DateDataNode("column", DatePattern.NATIVE);
        assertNotNull(dateDataNode);
        final Date date = new Date();
        dateDataNode.setValue(date);
        assertEquals(date, dateDataNode.getValue());
    }
}
