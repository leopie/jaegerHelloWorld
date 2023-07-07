package com.leopie.tracer;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public abstract class TracedController {

    protected final Tracer tracer;

    public TracedController(Tracer tracer) {
        this.tracer = tracer;
    }

    protected Span buildSpanFromHttpServletRequest(String spanName, HttpServletRequest request) {
        SpanContext spanContext = extractParentSpanContext(request);
        return buildSpanFromSpanContext(spanName, spanContext);
    }

    private SpanContext extractParentSpanContext(HttpServletRequest request) {
       return tracer.extract(Format.Builtin.HTTP_HEADERS, new Extractor(request));
    }

    private Span buildSpanFromSpanContext(String spanName, SpanContext spanContext) {
        return tracer.buildSpan(spanName).asChildOf(spanContext).start();
    }

    protected void injectSpanContextInHeaders(HttpHeaders headers) {
        this.tracer.inject(this.tracer.activeSpan().context(), Format.Builtin.HTTP_HEADERS, new Injector(headers));
    }
}
