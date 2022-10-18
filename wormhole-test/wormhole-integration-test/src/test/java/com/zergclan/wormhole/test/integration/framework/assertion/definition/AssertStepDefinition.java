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

package com.zergclan.wormhole.test.integration.framework.assertion.definition;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Assert step definition.
 */
@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public final class AssertStepDefinition {
    
    @XmlAttribute(name = "id")
    private String id;
    
    @XmlAttribute(name = "plan-identifier")
    private String planIdentifier;
    
    @XmlAttribute(name = "expected-file")
    private String expectedFile;
    
    @XmlElement(name = "expected-data")
    private final List<ExpectedDataDefinition> expectedData = new LinkedList<>();
}
