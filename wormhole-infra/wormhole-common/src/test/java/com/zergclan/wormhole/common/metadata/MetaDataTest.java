package com.zergclan.wormhole.common.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MetaDataTest {

    @Test
    public void assertMySQLDataSource() {
        DataSourceMetaData dataSourceMetaData = new DataSourceMetaData("mysql_db");
        assertEquals("127.0.0.1", dataSourceMetaData.getHostName());
        assertEquals(3306, dataSourceMetaData.getPort());
        assertEquals("mysql_db", dataSourceMetaData.getCatalog());
    }
}
