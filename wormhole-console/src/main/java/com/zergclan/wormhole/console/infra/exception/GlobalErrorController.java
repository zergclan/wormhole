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

package com.zergclan.wormhole.console.infra.exception;

import com.zergclan.wormhole.common.constant.MarkConstant;
import com.zergclan.wormhole.console.api.vo.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Global error controller.
 */
@Slf4j
@Controller
@RequestMapping(value = "/error")
public final class GlobalErrorController extends BasicErrorController {

    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Autowired
    public GlobalErrorController(final ErrorAttributes errorAttributes, final ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
    }

    @Override
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(final HttpServletRequest request, final HttpServletResponse response) {
        return super.errorHtml(request, response);
    }

    @Override
    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(final HttpServletRequest request) {
        return new ResponseEntity<>(initializeReturnData(request, parseErrorBody(request)), getStatus(request));
    }

    private Map<String, Object> parseErrorBody(final HttpServletRequest request) {
        ErrorAttributeOptions errorAttributeOptions = isIncludeStackTrace(request, MediaType.APPLICATION_JSON)
                ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE) : ErrorAttributeOptions.defaults();
        return getErrorAttributes(request, errorAttributeOptions);
    }

    private Map<String, Object> initializeReturnData(final HttpServletRequest request, final Map<String, Object> errorBody) {
        Throwable throwable = getException(request);
        if (throwable instanceof WormholeWebException) {
            WormholeWebException exception = (WormholeWebException) throwable;
            return this.createResult(exception.getCode(), exception.getMessage(), exception.getMessage());
        }
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) throwable;
            List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
            StringJoiner stringJoiner = new StringJoiner(MarkConstant.SPACE);
            for (ObjectError objectError : allErrors) {
                String defaultMessage = objectError.getDefaultMessage();
                stringJoiner.add(defaultMessage);
            }
            return this.createResult(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getMessage(), stringJoiner.toString());
        }
        return this.createResult(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage(), errorBody.get("error"));
    }

    private Throwable getException(final HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        return exception == null ? getAttribute(requestAttributes, "javax.servlet.error.exception") : exception;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(final RequestAttributes requestAttributes, final String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    private Map<String, Object> createResult(final Object code, final Object message, final Object data) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("code", code);
        resultMap.put("message", message);
        resultMap.put("data", data);
        return resultMap;
    }
}
