package com.myapp.filters;

import static io.micronaut.http.annotation.Filter.MATCH_ALL_PATTERN;

import java.util.UUID;

import org.reactivestreams.Publisher;
import org.slf4j.MDC;

import io.micronaut.context.propagation.slf4j.MdcPropagationContext;
import io.micronaut.core.propagation.PropagatedContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;


@Filter(MATCH_ALL_PATTERN)
public class MDCFilter implements HttpServerFilter {

  final public static String REQUEST_ID = "requestId";

  @Override
  public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
    try {
      MDC.put(REQUEST_ID, UUID.randomUUID().toString());
      try (PropagatedContext.Scope ignore = PropagatedContext.get().plus(new MdcPropagationContext()).propagate()) {
        return chain.proceed(request);
      }
    } finally {
      MDC.remove(REQUEST_ID);
    }
  }
}