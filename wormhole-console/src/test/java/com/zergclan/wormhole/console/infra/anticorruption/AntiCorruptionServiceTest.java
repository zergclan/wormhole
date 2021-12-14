/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zergclan.wormhole.console.infra.anticorruption;

import com.zergclan.wormhole.console.api.vo.LoginVO;
import com.zergclan.wormhole.console.application.domain.entity.UserInfo;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AntiCorruptionServiceTest {
    
    @Test
    public void assertUserLoginVOToDTOByUsername() {
        LoginVO loginVO = new LoginVO();
        loginVO.setLoginName("jack");
        loginVO.setPassword("123456");
        loginVO.setLoginType(0);
        Optional<UserInfo> userInfoOptional = AntiCorruptionService.userLoginVOToPO(loginVO);
        assertTrue(userInfoOptional.isPresent());
        UserInfo userInfo = userInfoOptional.get();
        assertEquals(loginVO.getLoginName(), userInfo.getUsername());
        assertEquals(loginVO.getPassword(), userInfo.getPassword());
    }
    
    @Test
    public void assertUserLoginVOToDTOByEmail() {
        LoginVO loginVO = new LoginVO();
        loginVO.setLoginName("jack");
        loginVO.setPassword("123456");
        loginVO.setLoginType(1);
        Optional<UserInfo> userInfoOptional = AntiCorruptionService.userLoginVOToPO(loginVO);
        assertFalse(userInfoOptional.isPresent());
    }
}
