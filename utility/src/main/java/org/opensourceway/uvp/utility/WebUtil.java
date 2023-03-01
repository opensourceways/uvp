package org.opensourceway.uvp.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;

@Component
public class WebUtil {
    @Value("${spring.codec.max-in-memory-size}")
    private String maxInMemorySize;

    public final WebClient createWebClient() {
        return WebClient.builder()
                .exchangeStrategies(customStrategy())
                .build();
    }

    public final WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(customStrategy())
                .build();
    }

    private ExchangeStrategies customStrategy() {
        return ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize((int) DataSize.parse(maxInMemorySize).toBytes()))
                .build();
    }

    public DataBuffer getFileContext(String url) {
        return internalGetFileContext(url).block();
    }

    public DataBuffer getFileContext(String url, Duration timeout) {
        return internalGetFileContext(url).block(timeout);
    }

    private Mono<DataBuffer> internalGetFileContext(String url) {
        return createWebClient().get()
                .uri(URI.create(url))
                .retrieve()
                .bodyToMono(DataBuffer.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> !(throwable instanceof WebClientResponseException.NotFound)));
    }
}
