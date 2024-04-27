package com.objecteffects.temperature.prom;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class HttpClientPrometheus {
    private final static Logger log = LogManager.getLogger();

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5)).version(Version.HTTP_2)
            .followRedirects(Redirect.NORMAL).build();

    public HttpResponse<String> sendAndReceive(final String query,
            final String params) throws IOException, InterruptedException {
        final String fullQuery = String
                .format("http://192.168.50.9:9090/api/v1/%s", query);

        log.debug("query: " + fullQuery);

        final HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .uri(URI.create(fullQuery)).timeout(Duration.ofSeconds(5))
                .build();

        final HttpResponse<String> response = this.client.send(request,
                BodyHandlers.ofString());

        log.debug("response: {}", response);

        final Gson gson = new Gson();

        final PromResponseStatus target = gson.fromJson(response.body(),
                PromResponseStatus.class);

        if (target.getStatus().compareTo("error") == 0) {
            throw new RuntimeException("error: " + target.getError());
        }

        return response;
    }
}
