package io.github.wannidev.simplehandlingexception.hanlder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.github.wannidev.simplehandlingexception.util.SimpleMessageUtils;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultUncheckedExceptionHandler<E extends RuntimeException> extends AbstractExceptionHandler<E> {

	private static final Log log = LogFactory.getLog(DefaultUncheckedExceptionHandler.class);

	public DefaultUncheckedExceptionHandler() {

		super(RuntimeException.class.getSimpleName());
		log.info("Created");
	}

	public DefaultUncheckedExceptionHandler(String exceptionName) {
		super(exceptionName);
	}

	@Override
	public HttpStatus getStatus(E ex) {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getMessage(E ex) {
		return SimpleMessageUtils.getMessage("io.github.wannidev.defaultRuntimeException", ex.getMessage());
	}
}
