package ru.practicum.explore_with_me;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.stats.EndpointHitDto;
import ru.practicum.explore_with_me.stats.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.explore_with_me.DateConstant.DATE_TIME_PATTERN;

@Service
public class StatsClient {

    private final RestTemplate rest;
    private final String serverUrl;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public StatsClient(@Value("${stats-server.url}")String serverUrl) {
        this.serverUrl = serverUrl;
        this.rest = new RestTemplate();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dtf)));
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        String path = serverUrl + "/stats?start=" + start.format(dtf) + "&end=" + end.format(dtf);
        if (unique != null) {
            path += "&unique=" + unique;
        }
        if (uris != null && !uris.isEmpty()) {
            path += "&uris=" + String.join("&uris=", uris);
        }
        return objectMapper.convertValue(makeAndSendRequest(HttpMethod.GET, path, null).getBody(), new TypeReference<>() {
        });
    }

    public ResponseEntity<Object> saveHit(EndpointHitDto hit) {

        try {
            System.out.println(objectMapper.writeValueAsString(hit));
            return makeAndSendRequest(HttpMethod.POST, serverUrl + "/hit", objectMapper.writeValueAsString(hit));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;
        try {
            statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}