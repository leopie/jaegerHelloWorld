package com.leopie.bigBrother;

import com.leopie.repository.Person;
import com.leopie.tracer.TracedController;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bigBrother")
public class BigBrotherController extends TracedController {
    private final BigBrotherService service;

    public BigBrotherController(
            @Autowired BigBrotherService service,
            @Autowired Tracer tracer) {
        super(tracer);
        this.service = service;
    }

    @GetMapping("/getPerson/{name}")
    public Person getPerson(@PathVariable("name") String name, HttpServletRequest request) {
        Span span = this.buildSpanFromHttpServletRequest("/getPerson", request);
        try (Scope s = tracer.activateSpan(span)) {
            return service.getPerson(name);
        } finally {
            span.finish();
        }
    }

}
