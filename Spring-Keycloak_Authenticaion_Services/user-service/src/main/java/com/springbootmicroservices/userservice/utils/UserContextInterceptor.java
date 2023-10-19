package com.springbootmicroservices.userservice.utils;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class UserContextInterceptor {

//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        log.info("2 filter User Service ");
//        HttpHeaders headers = request.getHeaders();
//        headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
//        log.info("2 filter User Service {}",UserContextHolder.getContext().getCorrelationId());
//        return execution.execute(request, body);
//    }
}
