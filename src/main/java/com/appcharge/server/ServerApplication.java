package com.appcharge.server;

import com.appcharge.server.middleware.DecryptionMiddleware;
import com.appcharge.server.middleware.ErrorHandlerMiddleware;
import com.appcharge.server.service.SignatureHashingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<DecryptionMiddleware> decryptionFilterRegistration(SignatureHashingService signatureHashingService) {
		FilterRegistrationBean<DecryptionMiddleware> registrationBean = new FilterRegistrationBean<>();
		DecryptionMiddleware decryptionMiddleware = new DecryptionMiddleware(signatureHashingService);
		registrationBean.setFilter(decryptionMiddleware);
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(1);

		decryptionMiddleware.addExcludedRoute("/mocker/offer");
		decryptionMiddleware.addExcludedRoute("/mocker/orders");
		decryptionMiddleware.addExcludedRoute("/mocker/analytics");
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<ErrorHandlerMiddleware> errorHandlerFilterRegistration() {
		FilterRegistrationBean<ErrorHandlerMiddleware> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ErrorHandlerMiddleware());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
