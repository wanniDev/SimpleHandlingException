package io.github.wannidev.simplehandlingexception.hanlder;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.github.wannidev.simplehandlingexception.SimpleFieldError;
import io.github.wannidev.simplehandlingexception.util.SimpleMessageUtils;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ConstraintViolationExceptionHandler<E extends ConstraintViolationException> extends AbstractExceptionHandler<E> {

	private static final Log log = LogFactory.getLog(ConstraintViolationExceptionHandler.class);

	public ConstraintViolationExceptionHandler() {

		super(ConstraintViolationException.class.getSimpleName());
		log.info("Created");
	}

	public ConstraintViolationExceptionHandler(String exceptionName) {
		super(exceptionName);
	}

	@Override
	protected HttpStatus getStatus(E exception) {
		return HttpStatus.UNPROCESSABLE_ENTITY;
	}

	@Override
	protected Collection<SimpleFieldError> getErrors(E exception) {
		return SimpleFieldError.getErrors(exception.getConstraintViolations());
	}

	@Override
	protected String getMessage(E exception) {
		return SimpleMessageUtils.getMessage("io.github.wannidev.validationError");
	}
}
