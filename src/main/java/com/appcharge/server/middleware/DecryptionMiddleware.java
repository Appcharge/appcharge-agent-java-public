package com.appcharge.server.middleware;

import com.appcharge.server.service.SignatureHashingService;
import com.appcharge.server.service.entity.SignatureResponse;
import com.appcharge.server.service.impl.ConfigurationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.appcharge.server.service.ConfigurationService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class DecryptionMiddleware extends OncePerRequestFilter {

    private final ConfigurationService configurationService;
    private final SignatureHashingService signatureHashingService;
    private final ObjectMapper objectMapper;
    private final List<String> excludedRoutes;

    @Autowired
    public DecryptionMiddleware(SignatureHashingService signatureHashingService, ConfigurationService configurationService) {
        this.signatureHashingService = signatureHashingService;
        this.objectMapper = new ObjectMapper();
        this.excludedRoutes = new ArrayList<>();
        this.configurationService = configurationService;
    }

    public void addExcludedRoute(String route) {
        excludedRoutes.add(route);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            HttpServletRequest httpRequest = request;
            String requestUrl = httpRequest.getRequestURI();
            String requestMethod = httpRequest.getMethod();

            if (isExcludedRoute(requestUrl) || Objects.equals(requestMethod, "GET")) {
                filterChain.doFilter(request, response);
                return;
            }
            String publisherToken = request.getHeader("x-publisher-token");
            if (publisherToken == null || publisherToken.isEmpty() || !publisherToken.equals(configurationService.getPublisherToken())) {
              throw new Exception("x-publisher-token is not passed or not correct.");
            }

            String requestBody = readRequestBody(request);

            String serializedJson = objectMapper.writeValueAsString(objectMapper.readValue(requestBody, Object.class));
            SignatureResponse signatureResponse = signatureHashingService.signPayload(request.getHeader("signature"),
                    serializedJson);
            if (!signatureResponse.getSignature().equals(signatureResponse.getExpectedSignature())) {
                throw new Exception("Signatures don't match");
            }
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            request = updateRequestBody(request, requestBodyBytes);
        } catch (Exception ex) {
            System.out.println("Exception occurred: " + ex.getMessage());
            System.out.println("Stack trace: " + Arrays.toString(ex.getStackTrace()));

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String errorMessage = "{\"message\": \"Bad request: " + ex.getMessage() + "\"}";
            response.setContentType("application/json");
            response.getWriter().write(errorMessage);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }

    private boolean isExcludedRoute(String requestUrl) {
        for (String excludedRoute : excludedRoutes) {
            if (requestUrl.startsWith(excludedRoute)) {
                return true;
            }
        }
        return false;
    }

    private HttpServletRequest updateRequestBody(HttpServletRequest request, byte[] requestBodyBytes) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public ServletInputStream getInputStream() {
                return new CustomServletInputStream(new ByteArrayInputStream(requestBodyBytes));
            }

            @Override
            public int getContentLength() {
                return requestBodyBytes.length;
            }

            @Override
            public long getContentLengthLong() {
                return requestBodyBytes.length;
            }
        };
    }

    @RequiredArgsConstructor
    private static class CustomServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            // Not needed for this implementation
        }

        @Override
        public int read() {
            return inputStream.read();
        }
    }
}
