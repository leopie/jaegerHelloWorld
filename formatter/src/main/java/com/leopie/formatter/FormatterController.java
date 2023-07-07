package com.leopie.formatter;

import com.leopie.tracer.TracedController;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/formatter")
public class FormatterController extends TracedController {
    private final FormatterService service;

    public FormatterController(
            @Autowired FormatterService service,
            @Autowired Tracer tracer) {
        super(tracer);
        this.service = service;
    }

    @GetMapping("/formatGreeting")
    public String formatGreeting(
            @RequestParam String name,
            @RequestParam String title,
            @RequestParam String description,
            HttpServletRequest request) {
        Span span = this.buildSpanFromHttpServletRequest("/formatGreeting", request);
        try (Scope s = tracer.activateSpan(span)) {
            return service.formatGreeting(name, title, description);
        } finally {
            span.finish();
        }
    }

}
