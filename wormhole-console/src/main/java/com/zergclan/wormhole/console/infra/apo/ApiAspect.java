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

package com.zergclan.wormhole.console.infra.apo;

import com.zergclan.wormhole.tool.util.Arrays;
import com.zergclan.wormhole.tool.util.DateUtil;
import com.zergclan.wormhole.tool.util.StringUtil;
import com.zergclan.wormhole.console.api.vo.ResultCode;
import com.zergclan.wormhole.console.infra.exception.WormholeWebException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Aspect class for console api.
 */
@Aspect
@Component
@Slf4j
public final class ApiAspect {
    
    private static final String LOG_PREFIX = "wormhole-console-api";
    
    private static final String REQUEST_HEAD_TRACE = "trace";
    
    private static final String[] NO_TRACE_URL = {"/user/login", "/scheduling/plan/trigger/test_plan"};
    
    /**
     * Point cut.
     */
    @Pointcut("execution(* com.zergclan.wormhole.console.api.contorller..*.*(..))")
    public void printLog() {
    
    }
    
    @SneakyThrows
    @Around("printLog()")
    private Object doAround(final ProceedingJoinPoint proceedingJoinPoint) {
        long startTime = DateUtil.currentTimeMillis();
        try {
            markTrace(getHttpServletRequest());
            Object[] requestArgs = proceedingJoinPoint.getArgs();
            log.info("[{}] accept request start. args: {}", LOG_PREFIX, requestArgs);
            Object result = proceedingJoinPoint.proceed();
            log.info("[{}] execute request is successful. response:[{}]", LOG_PREFIX, result);
            return result;
        } finally {
            Long executionTime = calculateExecutionTime(startTime);
            log.info("[{}] execute request completed expend time [{}] ms", LOG_PREFIX, executionTime);
            this.removeTrace();
        }
    }
    
    private HttpServletRequest getHttpServletRequest() throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == servletRequestAttributes) {
            throw new ClassNotFoundException("Caused by: javax.servlet.http,HttpServletRequest not found");
        }
        return servletRequestAttributes.getRequest();
    }
    
    private void markTrace(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (Arrays.contains(NO_TRACE_URL, uri)) {
            return;
        }
        String trace = request.getHeader(REQUEST_HEAD_TRACE);
        if (StringUtil.isBlank(trace)) {
            throw new WormholeWebException(ResultCode.NOT_ACCEPTABLE.getCode(), ResultCode.NOT_ACCEPTABLE.getMessage());
        }
        MDC.put(REQUEST_HEAD_TRACE, trace);
    }
    
    private Long calculateExecutionTime(final long startTime) {
        return DateUtil.currentTimeMillis() - startTime;
    }
    
    private void removeTrace() {
        MDC.remove(REQUEST_HEAD_TRACE);
    }
}
