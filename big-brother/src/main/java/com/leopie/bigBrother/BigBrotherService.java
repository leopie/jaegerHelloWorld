package com.leopie.bigBrother;

import com.leopie.repository.Person;
import com.leopie.repository.PersonRepository;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BigBrotherService {
    private final Tracer tracer;
    private final PersonRepository repository;

    public BigBrotherService(
            @Autowired Tracer tracer,
            @Autowired PersonRepository personRepository) {
        this.tracer = tracer;
        this.repository = personRepository;
    }

    public Person getPerson(String name) {
        Span span = tracer.buildSpan("get-person").start();
        try(Scope s = tracer.activateSpan(span)) {
            Optional<Person> personOpt = repository.findById(name);
            return personOpt.orElseGet(() -> new Person(name));
        } finally {
            span.finish();
        }
    }

}
