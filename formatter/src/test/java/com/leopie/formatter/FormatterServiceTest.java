package com.leopie.formatter;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class FormatterServiceTest {
    @Mock
    private Tracer tracerMock;
    @Mock
    private Tracer.SpanBuilder spanBuilderMock;
    @Mock
    private Span spanMocked;
    @InjectMocks
    private FormatterService serviceUnderTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(spanMocked).when(spanBuilderMock).start();
        Mockito.doReturn(spanBuilderMock).when(tracerMock).buildSpan(Mockito.anyString());
    }

    @Test
    public void formatGreetingWithNameTitleAndDescription() {
        String actual = serviceUnderTest.formatGreeting("name", "title", "description");
        Assertions.assertEquals("Hello, title name description", actual);
    }

    @Test
    public void formatGreetingWithNameAndTitle_descriptionIsEmpty() {
        String actual = serviceUnderTest.formatGreeting("name", "title", "");
        Assertions.assertEquals("Hello, title name", actual);
    }

    @Test
    public void formatGreetingWithNameAndTitle_descriptionIsNull() {
        String actual = serviceUnderTest.formatGreeting("name", "title", null);
        Assertions.assertEquals("Hello, title name", actual);
    }

    @Test
    public void formatGreetingWithNameAndDescription_titleIsEmpty() {
        String actual = serviceUnderTest.formatGreeting("name", "", "description");
        Assertions.assertEquals("Hello, name description", actual);
    }

    @Test
    public void formatGreetingWithNameAndDescription_titleIsNull() {
        String actual = serviceUnderTest.formatGreeting("name", null, "description");
        Assertions.assertEquals("Hello, name description", actual);
    }

    @Test
    public void formatGreetingWithOnlyName() {
        String actual = serviceUnderTest.formatGreeting("name", "", "");
        Assertions.assertEquals("Hello, name", actual);
    }

    @Test
    public void formatGreeting_nameIsNull() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceUnderTest.formatGreeting(null, null, null);
        });
        Assertions.assertEquals("Name parameter cannot be null or empty",exception.getMessage());
    }
}
