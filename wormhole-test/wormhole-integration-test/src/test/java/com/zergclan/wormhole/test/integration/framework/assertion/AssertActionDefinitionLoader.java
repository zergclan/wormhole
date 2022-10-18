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

package com.zergclan.wormhole.test.integration.framework.assertion;

import com.zergclan.wormhole.test.integration.framework.assertion.definition.AssertActionDefinition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Assert action definition loader.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssertActionDefinitionLoader {
    
    /**
     * Load {@link AssertActionDefinition}.
     *
     * @param scenario scenario
     * @return {@link AssertActionDefinition}
     */
    public static AssertActionDefinition load(final String scenario) {
        return loadDefinition(scenario, "assert-action.xml");
    }
    
    @SneakyThrows({IOException.class, JAXBException.class})
    private static AssertActionDefinition loadDefinition(final String scenario, final String fileName) {
        URL url = Objects.requireNonNull(AssertActionDefinitionLoader.class.getClassLoader().getResource(String.format("env/scenario/%s/", scenario)));
        return unmarshal(url.getPath() + fileName, AssertActionDefinition.class);
    }
    
    @SuppressWarnings("all")
    private static <T> T unmarshal(final String assertDefinitionFile, final Class<T> clazz) throws IOException, JAXBException {
        try (FileReader reader = new FileReader(assertDefinitionFile)) {
            return (T) JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(reader);
        }
    }
}
