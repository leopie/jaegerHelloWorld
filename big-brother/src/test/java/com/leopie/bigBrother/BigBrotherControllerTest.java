package com.leopie.bigBrother;

import com.leopie.repository.Person;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BigBrotherControllerTest {
    @Mock
    private Span spanMock;
    @Mock
    private SpanContext spanContext;
    @Mock
    private Tracer.SpanBuilder spanBuilderMock;
    @Mock
    private Tracer tracerMock;
    @Mock
    private BigBrotherService serviceMock;
    @InjectMocks
    private BigBrotherController controllerUnderTest;
    private MockMvc mockMvc;

    public static final Person DAVE = new Person("Dave");
    public static final String HTTP_REQUEST_URL_FOR_GET_PERSON_ENDPOINT = "http://localhost:8081/api/v1/bigBrother/getPerson";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controllerUnderTest).build();
        Mockito.doReturn(DAVE)
                .when(this.serviceMock)
                .getPerson(anyString());
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
        URI requestUri = UriComponentsBuilder.fromHttpUrl(HTTP_REQUEST_URL_FOR_GET_PERSON_ENDPOINT)
                .path("/Dave")
                .build(Collections.emptyMap());
        MvcResult mvcResult = mockMvc.perform(get(requestUri))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(DAVE.toString(), mvcResult.getResponse().getContentAsString());
        Mockito.verify(serviceMock, Mockito.times(1))
                .getPerson(Mockito.anyString());
    }

}
