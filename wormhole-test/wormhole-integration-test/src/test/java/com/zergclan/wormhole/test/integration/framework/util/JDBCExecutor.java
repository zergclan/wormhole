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

package com.zergclan.wormhole.test.integration.framework.util;

import com.zergclan.wormhole.test.integration.framework.container.DockerContainerDefinition;
import com.zergclan.wormhole.test.integration.framework.container.storage.atomic.MySQLITContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.RemoteDockerImage;
import org.testcontainers.utility.DockerImageName;

public final class JDBCExecutor {
    
    public static void main(String[] args) {
        GenericContainer<?> container = new GenericContainer<>(new RemoteDockerImage(DockerImageName.parse("mysql:5.7")));
        container.addExposedPorts(3306);
        container.withCommand("--default-authentication-plugin=mysql_native_password");
        container.addEnv("LANG", "C.UTF-8");
        container.addEnv("MYSQL_ROOT_PASSWORD", "root");
        container.addEnv("MYSQL_ROOT_HOST", "%");
        container.start();
        
        
    
        final String host = container.getHost();
        final Integer firstMappedPort = container.getFirstMappedPort();
    
        System.out.println(host);
        System.out.println(firstMappedPort);
    }
}
