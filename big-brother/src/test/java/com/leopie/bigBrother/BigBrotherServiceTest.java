package com.leopie.bigBrother;

import com.leopie.repository.Person;
import com.leopie.repository.PersonRepository;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

public class BigBrotherServiceTest {
    public static final Person CICCIO = new Person("ciccio", "re", "indiscusso");
    @Mock
    private PersonRepository personRepository;
    @Mock
    private Tracer tracerMock;
    @Mock
    private Tracer.SpanBuilder spanBuilderMock;
    @Mock
    private Span spanMocked;
    @InjectMocks
    private BigBrotherService serviceUnderTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(Optional.of(CICCIO))
                .when(personRepository)
                .findById(CICCIO.getName());
        Mockito.doReturn(spanMocked)
                .when(spanBuilderMock)
                .start();
        Mockito.doReturn(spanBuilderMock)
                .when(tracerMock)
                .buildSpan(anyString());
    }

    @Test
    public void getPersonTest_presentInDB() {
        Person actual = serviceUnderTest.getPerson(CICCIO.getName());
        Person expected = new Person(
                CICCIO.getName(),
                CICCIO.getTitle(),
                CICCIO.getDescription()
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getPersonTest_nonPresentInDB() {
        Person actual = serviceUnderTest.getPerson("Dave");
        Person expected = new Person("Dave");
        Assertions.assertEquals(expected, actual);
    }
}
