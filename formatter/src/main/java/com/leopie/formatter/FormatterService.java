package com.leopie.formatter;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormatterService {
    private final Tracer tracer;

    public FormatterService(@Autowired Tracer tracer) {
        this.tracer = tracer;
    }

    public String formatGreeting(
            String name,
            String title,
            String description) {
        Span span = tracer.buildSpan("format-greeting").start();
        try(Scope s = tracer.activateSpan(span)) {
            this.checkNameIsNotEmpty(name);
            StringBuilder greeting = new StringBuilder("Hello, ");
            this.addTitleIfNotEmpty(title, greeting);
            greeting.append(name);
            this.addDescriptionIfNotEmpty(description, greeting);
            return greeting.toString();
        } finally {
            span.finish();
        }
    }

    private void checkNameIsNotEmpty(String name) {
        if(name==null || name.isEmpty()) {
            throw new IllegalArgumentException("Name parameter cannot be null or empty");
        }
    }

    private void addTitleIfNotEmpty(String title, StringBuilder greeting) {
        if (title !=null && !title.isEmpty()) {
            greeting.append(title).append(" ");
        }
    }

    private void addDescriptionIfNotEmpty(String description, StringBuilder greeting) {
        if (description !=null && !description.isEmpty()) {
            greeting.append(" ").append(description);
        }
    }

}
