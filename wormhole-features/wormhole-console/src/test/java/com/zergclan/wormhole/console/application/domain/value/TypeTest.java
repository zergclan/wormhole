package com.zergclan.wormhole.console.application.domain.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TypeTest {

    @Test
    public void assertDatasource() {
        assertEquals(0, DatasourceType.MYSQL.getCode());
    }

    @Test
    public void assertLogin() {
        assertEquals(0, LoginType.USERNAME.getCode());
        assertEquals(1, LoginType.EMAIL.getCode());
    }

    @Test
    public void assertRootUser() {
        RootUser user = RootUser.ROOT;
        assertEquals("root", user.getLoginName());
        assertEquals("root", user.getSecretKey());
    }
}
