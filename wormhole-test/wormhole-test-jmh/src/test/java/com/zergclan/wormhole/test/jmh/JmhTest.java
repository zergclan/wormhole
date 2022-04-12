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

package com.zergclan.wormhole.test.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 5)
@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JmhTest {
    
    private static Options opt;
    
    private static Map<String, String> testMap = new LinkedHashMap<>();
    
    private void initRunner() {
        opt = new OptionsBuilder().include(JmhTest.class.getSimpleName()).result(System.getProperty("user.dir") + "/src/test/resources/jmh_result.json").resultFormat(ResultFormatType.JSON).build();
    }
    
    private void testRun() throws RunnerException {
        new Runner(opt).run();
    }
    
    @Benchmark
    public void assertIterator(final Blackhole blackhole) {
        Iterator<Map.Entry<String, String>> iterator = testMap.entrySet().iterator();
        String key;
        String value;
        Map.Entry<String, String> next;
        while (iterator.hasNext()) {
            next = iterator.next();
            key = next.getKey();
            value = next.getKey();
        }
        blackhole.consume("");
    }
    
    @Benchmark
    public void assertForeach(final Blackhole blackhole) {
        String key;
        String value;
        for (Map.Entry<String, String> entry : testMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
        }
        blackhole.consume("");
    }
}
