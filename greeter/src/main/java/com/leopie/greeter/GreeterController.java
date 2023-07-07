package com.leopie.greeter;

import com.leopie.repository.Person;
import com.leopie.tracer.TracedController;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/jaeger")
public class GreeterController extends TracedController {

    public static final String HTTP_REQUEST_URL_FOR_GET_PERSON_ENDPOINT = "http://localhost:8081/api/v1/bigBrother/getPerson";
    public static final String HTTP_REQUEST_URL_FOR_FORMAT_GREETING_ENDPOINT = "http://localhost:8082/api/v1/formatter/formatGreeting";

    private final RestTemplate restTemplate;

    public GreeterController(
            @Autowired Tracer tracer,
            @Autowired RestTemplate restTemplate) {
        super(tracer);
        this.restTemplate = restTemplate;
    }

    @GetMapping("/sayHello/{name}")
    public String sayHello(@PathVariable String name) {
        Span rootSpan = tracer.buildSpan("say-hello").start();
        try(Scope s = tracer.scopeManager().activate(rootSpan)) {
            Person person = getPerson(name);
            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("name", person.getName());
            fields.put("title", person.getTitle());
            fields.put("description", person.getDescription());
            rootSpan.log(fields);

            String formattedGreeting = formatGreeting(person);
            rootSpan.setTag("response", formattedGreeting);

            return formattedGreeting;
        } finally {
            rootSpan.finish();
        }
    }

    private Person getPerson(String name) {
        URI requestUri = UriComponentsBuilder.fromHttpUrl(HTTP_REQUEST_URL_FOR_GET_PERSON_ENDPOINT + "/" + name)
                .build(Collections.emptyMap());
        HttpHeaders headers = new HttpHeaders();
        this.injectSpanContextInHeaders(headers);
        ResponseEntity<Person> responseEntity = this.restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Person.class
        );
        return responseEntity.getBody();
    }

    private String formatGreeting(Person person) {
        URI requestUri = UriComponentsBuilder.fromHttpUrl(HTTP_REQUEST_URL_FOR_FORMAT_GREETING_ENDPOINT)
                .queryParam("name", person.getName())
                .queryParam("title", person.getTitle())
                .queryParam("description", person.getDescription())
                .build(Collections.emptyMap());
        HttpHeaders headers = new HttpHeaders();
        this.injectSpanContextInHeaders(headers);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                requestUri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        return responseEntity.getBody();
    }

}
