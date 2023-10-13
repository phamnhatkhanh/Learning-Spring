package com.springbootmicroservices.userservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//        LOGGER.debug("1 filter User Service ");
//
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//
////        UserContextHolder.getContext().setCorrelationId(  httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
//        UserContextHolder.getContext().setCorrelationId("cbbeddad-707f-4ea8-8e4f-ea279750c482");
//
//        LOGGER.debug("User Service | UserContextFilter | doFilter |Organization Service Incoming Correlation id: {}" ,UserContextHolder.getContext().getCorrelationId());
//        filterChain.doFilter(httpServletRequest, servletResponse);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {}
//
//    @Override
//    public void destroy() {}
}
