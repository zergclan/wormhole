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

package com.zergclan.wormhole.common.configuration.initializer;

import com.zergclan.wormhole.common.WormholeInitializer;
import com.zergclan.wormhole.common.configuration.AuthorizationConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlAuthorizationConfiguration;
import com.zergclan.wormhole.common.configuration.yaml.YamlUserConfiguration;
import com.zergclan.wormhole.common.metadata.authorization.user.WormholeUser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Initializer of {@link AuthorizationConfiguration}.
 */
public final class AuthorizationConfigurationInitializer implements WormholeInitializer<YamlAuthorizationConfiguration, AuthorizationConfiguration> {
    
    @Override
    public AuthorizationConfiguration init(final YamlAuthorizationConfiguration yamlConfiguration) {
        Collection<YamlUserConfiguration> users = yamlConfiguration.getUsers();
        Map<String, WormholeUser> wormholeUsers = new LinkedHashMap<>(users.size() + 1, 1);
        users.forEach(each -> wormholeUsers.put(each.getUsername(), new WormholeUser(each.getUsername(), each.getPassword())));
        return initWormholeRootUser(wormholeUsers);
    }
    
    private AuthorizationConfiguration initWormholeRootUser(final Map<String, WormholeUser> users) {
        users.put("wormhole@root", new WormholeUser("wormhole@root", "wormhole@root"));
        return new AuthorizationConfiguration(users);
    }
}
