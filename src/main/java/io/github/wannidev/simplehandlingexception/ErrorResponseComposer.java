package io.github.wannidev.simplehandlingexception;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import io.github.wannidev.simplehandlingexception.advice.DefaultExceptionHandlerControllerAdvice;
import io.github.wannidev.simplehandlingexception.hanlder.AbstractExceptionHandler;

public class ErrorResponseComposer<T extends Throwable> {

	private static final Log log = LogFactory.getLog(ErrorResponseComposer.class);

	private final Map<String, AbstractExceptionHandler<T>> handlers;

	public ErrorResponseComposer(List<AbstractExceptionHandler<T>> handlers) {

		this.handlers = handlers.stream().collect(
			Collectors.toMap(AbstractExceptionHandler::getExceptionName,
				Function.identity(), (handler1, handler2) -> AnnotationAwareOrderComparator
					.INSTANCE.compare(handler1, handler2) < 0 ?
					handler1 : handler2));

		log.info("Created");
	}

	/**
	 * 예외가 주어지면, 핸들러를 찾는다.
	 * 핸들러를 통해 응답 메세지를 빌드하고, 리턴한다.
	 */
	public Optional<ErrorResponse> compose(T ex) {

		AbstractExceptionHandler<T> handler = null;

		// 예외를 다루는 핸들러는 조회하는데, 핸들러를 못찾으면,
		// ex.getCause()를 통해 그 예외 자체를 할당한다.
		while (ex != null) {

			handler = handlers.get(ex.getClass().getSimpleName());

			if (handler != null) {
				break;
			}

			if (RuntimeException.class.isAssignableFrom(ex.getClass())) {
				handler = handlers.get(RuntimeException.class.getSimpleName());
			}

			ex = (T) ex.getCause();
		}

		if (handler != null) {
			return Optional.of(handler.getErrorResponse(ex));
		}

		return Optional.empty();
	}
}
