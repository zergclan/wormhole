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

package com.zergclan.wormhole.web.exception;

import com.zergclan.wormhole.web.vo.ResultCode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Global error controller for wormhole web.
 */
@Slf4j
@Controller
@RequestMapping(value = "/error")
public final class GlobalErrorController extends BasicErrorController {
    
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";
    
    private static final String ARGS_ERROR_DATA_DELIMITER = "#";
    
    @Autowired
    public GlobalErrorController(final ErrorAttributes errorAttributes, final ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
    }
    
    @Override
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(final HttpServletRequest request, final HttpServletResponse response) {
        return super.errorHtml(request, response);
    }
    
    @RequestMapping
    @ResponseBody
    @Override
    public ResponseEntity<Map<String, Object>> error(final HttpServletRequest request) {
        Map<String, Object> body = parseErrorBody(request);
        Map<String, Object> resultMap = initializeReturnData(request, body);
        return new ResponseEntity<>(resultMap, getStatus(request));
    }
    
    private Map<String, Object> initializeReturnData(final HttpServletRequest request, final Map<String, Object> body) {
        Throwable throwable = this.getException(request);
        if (throwable instanceof WormholeWebException) {
            WormholeWebException exception = (WormholeWebException) throwable;
            return this.createResultMap(exception.getCode(), this.parseMessage(body), this.parseError(body));
        }
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) throwable;
            List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
            StringJoiner stringJoiner = new StringJoiner(ARGS_ERROR_DATA_DELIMITER);
            for (ObjectError objectError : allErrors) {
                String defaultMessage = objectError.getDefaultMessage();
                stringJoiner.add(defaultMessage);
            }
            ResultCode badRequest = ResultCode.BAD_REQUEST;
            return this.createResultMap(badRequest.getCode(), badRequest.getMessage(), stringJoiner.toString());
        }
        return this.createResultMap(this.parseCode(body), this.parseMessage(body), this.parseError(body));
    }
    
    private Map<String, Object> createResultMap(final int code, final String message, final Object data) {
        Map<String, Object> resultMap = new HashMap<>(16);
        resultMap.put("code", code);
        resultMap.put("message", message);
        resultMap.put("data", data);
        return resultMap;
    }
    
    private Map<String, Object> parseErrorBody(final HttpServletRequest request) {
        ErrorAttributeOptions errorAttributeOptions = super.isIncludeStackTrace(request, MediaType.APPLICATION_JSON) ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)
                : ErrorAttributeOptions.defaults();
        return super.getErrorAttributes(request, errorAttributeOptions);
    }
    
    private Integer parseCode(final Map<String, Object> body) {
        Object status = body.get("status");
        return status instanceof Integer ? (Integer) status : ResultCode.ERROR.getCode();
    }
    
    private String parseMessage(final Map<String, Object> body) {
        Object message = body.get("message");
        return message instanceof String ? (String) message : ResultCode.ERROR.getMessage();
    }
    
    private Object parseError(final Map<String, Object> body) {
        Object error = body.get("error");
        return error instanceof String ? (String) error : ResultCode.ERROR.getMessage();
    }
    
    private Throwable getException(final HttpServletRequest request) {
        String exceptionClassName = "javax.servlet.error.exception";
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        return exception == null ? getAttribute(requestAttributes, exceptionClassName) : exception;
    }
    
    @SuppressWarnings("unchecked")
    private <T> T getAttribute(final RequestAttributes requestAttributes, final String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}
