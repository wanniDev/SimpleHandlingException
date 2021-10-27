package io.github.wannidev.simplehandlingexception.hanlder;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.github.wannidev.simplehandlingexception.SimpleFieldError;
import io.github.wannidev.simplehandlingexception.exception.ExplicitConstraintViolationException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExplicitConstraintViolationExceptionHandler extends ConstraintViolationExceptionHandler<ExplicitConstraintViolationException> {

	private static final Log log = LogFactory.getLog(ExplicitConstraintViolationExceptionHandler.class);

	public ExplicitConstraintViolationExceptionHandler() {

		super(ExplicitConstraintViolationException.class.getSimpleName());
		log.info("Created");
	}

	@Override
	public Collection<SimpleFieldError> getErrors(ExplicitConstraintViolationException exception) {
		return SimpleFieldError.getErrors(exception);
	}
}
