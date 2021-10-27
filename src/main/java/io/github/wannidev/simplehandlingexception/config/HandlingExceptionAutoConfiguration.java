package io.github.wannidev.simplehandlingexception.config;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import io.github.wannidev.simplehandlingexception.ErrorResponseComposer;
import io.github.wannidev.simplehandlingexception.advice.DefaultExceptionHandlerControllerAdvice;
import io.github.wannidev.simplehandlingexception.hanlder.AbstractExceptionHandler;
import io.github.wannidev.simplehandlingexception.util.SimpleMessageUtils;
import io.github.wannidev.simplehandlingexception.validator.SimpleValidator;

@Configuration
@AutoConfigureBefore({
	WebMvcAutoConfiguration.class,
	ErrorMvcAutoConfiguration.class,
	SecurityAutoConfiguration.class,
	SecurityFilterAutoConfiguration.class})
public class HandlingExceptionAutoConfiguration {

	private static final Log log = LogFactory.getLog(HandlingExceptionAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean(MessageSource.class)
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource
			= new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	@ConditionalOnMissingBean(LocalValidatorFactoryBean.class)
	public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();

		bean.setValidationMessageSource(messageSource);
		return bean;
	}

	@Bean
	@ConditionalOnMissingBean(ErrorResponseComposer.class)
	public <T extends Throwable> ErrorResponseComposer<T> errorResponseComposer(
		List<AbstractExceptionHandler<T>> handlers) {
		log.info("Configuring ErrorResponseComposer");
		return new ErrorResponseComposer<T>(handlers);
	}

	@Bean
	@ConditionalOnMissingBean(DefaultExceptionHandlerControllerAdvice.class)
	public <T extends Throwable>
	DefaultExceptionHandlerControllerAdvice<T> defaultExceptionHandlerControllerAdvice(ErrorResponseComposer<T> errorResponseComposer) {

		log.info("Configuring DefaultExceptionHandlerControllerAdvice");
		return new DefaultExceptionHandlerControllerAdvice<T>(errorResponseComposer);
	}

	@Bean
	@ConditionalOnMissingBean(SimpleMessageUtils.class)
	public SimpleMessageUtils simpleValidateUtils(MessageSource messageSource) {

		log.info("Configuring SimpleMessageUtils");
		return new SimpleMessageUtils(messageSource);
	}

	@Bean
	@ConditionalOnMissingBean(SimpleValidator.class)
	public SimpleValidator simpleValidator(LocalValidatorFactoryBean localValidatorFactoryBean) {

		log.info("Configuring simpleValidator");
		return new SimpleValidator(localValidatorFactoryBean);
	}
}
