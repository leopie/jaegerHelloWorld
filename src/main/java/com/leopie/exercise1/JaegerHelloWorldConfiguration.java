package com.leopie.exercise1;

import io.opentracing.Tracer;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerHelloWorldConfiguration {
    @Bean
    public Tracer initTracer() {
        SamplerConfiguration samplerConfiguration = new SamplerConfiguration()
                .withType("const")
                .withParam(1);
        ReporterConfiguration reporterConfiguration = new ReporterConfiguration()
                .withLogSpans(true);
        return new io.jaegertracing.Configuration("java-2-hello")
                .withSampler(samplerConfiguration)
                .withReporter(reporterConfiguration)
                .getTracer();
    }
}
