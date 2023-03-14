package com.gogitek.toeictest.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                e.getLocalizedMessage());
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var error = ResponseEntityBuilder
                .getBuilder()
                .setSuccess(false)
                .setDetails(e.getLocalizedMessage())
                .setMessage(e.getLocalizedMessage())
                .build();
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            writer.write(objectMapper.writeValueAsString(error));
        }
    }
}
