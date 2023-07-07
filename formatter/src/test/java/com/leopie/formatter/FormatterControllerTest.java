package com.leopie.formatter;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FormatterControllerTest {
    @Mock
    private Span spanMock;
    @Mock
    private SpanContext spanContext;
    @Mock
    private Tracer.SpanBuilder spanBuilderMock;
    @Mock
    private Tracer tracerMock;
    @Mock
    private FormatterService serviceMock;
    @InjectMocks
    private FormatterController controllerUnderTest;
    private MockMvc mockMvc;

    public static final String TEST_GREETING = "Hello, King Dave your majesty";
    public static final String HTTP_REQUEST_URL_FOR_FORMAT_GREETING_ENDPOINT = "http://localhost:8082/api/v1/formatter/formatGreeting";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
        Mockito.doReturn(TEST_GREETING)
                .when(serviceMock)
                .formatGreeting(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString()
                );
        Mockito.doReturn(this.spanMock)
                .when(this.spanBuilderMock)
                .start();
        Mockito.doReturn(this.spanBuilderMock)
                .when(this.spanBuilderMock)
                .asChildOf(any(SpanContext.class));
        Mockito.doReturn(this.spanBuilderMock)
                .when(this.tracerMock)
                .buildSpan(Mockito.anyString());
        Mockito.doReturn(this.spanContext)
                .when(this.tracerMock)
                .extract(any(), any());
    }

    @Test
    public void formatGreetingRestControllerTest() throws Exception {
        URI requestUri = UriComponentsBuilder.fromHttpUrl(HTTP_REQUEST_URL_FOR_FORMAT_GREETING_ENDPOINT)
                .queryParam("name", "Dave")
                .queryParam("title", "King")
                .queryParam("description", "your majesty")
                .build(Collections.emptyMap());
        MvcResult mvcResult = mockMvc.perform(get(requestUri))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(TEST_GREETING, mvcResult.getResponse().getContentAsString());
        Mockito.verify(serviceMock, Mockito.times(1))
                .formatGreeting(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString()
                );
    }
}
