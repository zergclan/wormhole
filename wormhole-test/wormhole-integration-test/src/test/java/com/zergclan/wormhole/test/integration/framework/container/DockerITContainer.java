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

package com.zergclan.wormhole.test.integration.framework.container;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.DockerHealthcheckWaitStrategy;
import org.testcontainers.images.RemoteDockerImage;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Abstract IT container based on docker.
 */
@Slf4j
@Getter(AccessLevel.PROTECTED)
public abstract class DockerITContainer extends GenericContainer<DockerITContainer> implements ITContainer {
    
    private final String identifier;
    
    private final String scenario;
    
    private final int port;
    
    public DockerITContainer(final String identifier, final String scenario, final String dockerImageName, final int port) {
        super(new RemoteDockerImage(DockerImageName.parse(dockerImageName)));
        this.identifier = identifier;
        this.scenario = scenario;
        this.port = port;
    }
    
    @Override
    public void start() {
        startDependencies();
        super.start();
        postStart();
    }
    
    protected void startDependencies() {
        getDependencies().stream().filter(each -> each instanceof DockerITContainer).map(each -> (DockerITContainer) each).forEach(this::startDependencies);
    }
    
    private void startDependencies(final DockerITContainer dockerITContainer) {
        if (!isHealthy(dockerITContainer)) {
            handleWaitStrategy(dockerITContainer);
        }
    }
    
    private boolean isHealthy(final DockerITContainer dockerITContainer) {
        try {
            return dockerITContainer.isHealthy();
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            log.info("Failed to Check healthy f container {} healthy.", dockerITContainer.getIdentifier(), ex);
            return false;
        }
    }
    
    private void handleWaitStrategy(final DockerITContainer dockerITContainer) {
        DockerHealthcheckWaitStrategy waitStrategy = new DockerHealthcheckWaitStrategy();
        log.info("Waiting for container {} healthy.", dockerITContainer.getDockerImageName());
        waitStrategy.withStartupTimeout(Duration.of(90, ChronoUnit.SECONDS));
        waitStrategy.waitUntilReady(dockerITContainer);
        log.info("Container {}:{} is startup.", dockerITContainer.getIdentifier(), dockerITContainer.getDockerImageName());
    }
    
    protected void postStart() {
    }
}
