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

package com.zergclan.wormhole.web.infra.anticorruption;

import com.zergclan.wormhole.web.api.vo.LoginVO;
import com.zergclan.wormhole.web.application.pojo.dto.UserLoginDTO;
import com.zergclan.wormhole.web.application.pojo.value.LoginType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AntiCorruptionService {

    /**
     * Convert {@link LoginVO} to {@link UserLoginDTO}.
     *
     * @param loginVO {@link LoginVO}
     * @return {@link UserLoginDTO}
     */
    public static UserLoginDTO userLoginVOToDTO(final LoginVO loginVO) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setLoginType(LoginType.USERNAME);
        userLoginDTO.setLoginName(loginVO.getLoginName());
        userLoginDTO.setPassword(loginVO.getPassword());
        return userLoginDTO;
    }

}
