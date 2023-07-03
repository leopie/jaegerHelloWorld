package com.leopie.exercise1;

import com.leopie.repository.Person;
import com.leopie.repository.PersonRepository;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/jaeger")
public record JaegerHelloWorldController(
        PersonRepository repository,
        Tracer tracer) {
    @GetMapping("/sayHello/{name}")
    public String sayHello(@PathVariable String name) {
        Span span = tracer.buildSpan("say-hello").start();
        try {
            Person person = getPerson(name);
            LinkedHashMap<String, String> fields = new LinkedHashMap<>();
            fields.put("name", person.getName());
            fields.put("title", person.getTitle());
            fields.put("description", person.getDescription());
            span.log(fields);
            String response = formatGreeting(person);
            span.setTag("response", response);
            return response;
        } finally {
            span.finish();
        }
    }

    private String formatGreeting(Person person) {
        StringBuilder greeting = new StringBuilder("Hello, ");
        if (!person.getTitle().isEmpty()) {
            greeting.append(person.getTitle()).append(" ");
        }
        greeting.append(person.getName());
        if (!person.getDescription().isEmpty()) {
            greeting.append(" ").append(person.getDescription());
        }
        return greeting.toString();
    }

    private Person getPerson(String name) {
        Optional<Person> personOpt = repository.findById(name);
        if (personOpt.isPresent()) {
            return personOpt.get();
        }
        return new Person(name);
    }


}
