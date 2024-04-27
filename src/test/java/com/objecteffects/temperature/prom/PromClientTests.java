package com.objecteffects.temperature.prom;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.objecteffects.temperature.prom.PromDataMatrix.PromSensor;
import com.objecteffects.temperature.prom.PromResponse.PromValue;;

/**
 * @author rusty
 */
class PromClientTests {
    private final static Logger log = LogManager
            .getLogger(PromClientTests.class);

    private final SimpleDateFormat jdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    @Test
    void testTemperatureRange() throws IOException, InterruptedException {
        final HttpClientPrometheus client = new HttpClientPrometheus();

        final String query = "query_range?query=temperature";

        final long timeStart = Instant.now().minusSeconds(299).getEpochSecond();
        final long timeEnd = Instant.now().getEpochSecond();

        final String params = String.format("step=60s&start=%s&end=%s",
                Long.toString(timeStart), Long.toString(timeEnd));

        final HttpResponse<String> response = client.sendAndReceive(query,
                params);

        log.debug("response body: {}", response.body());

        final Gson gson = new Gson();

        final PromDataMatrix promDataMatrix = gson.fromJson(response.body(),
                PromDataMatrix.class);
        log.debug("promData: {}", promDataMatrix);

        final List<PromValue> maxValues = new ArrayList<>();
        final List<PromValue> minValues = new ArrayList<>();

        promDataMatrix.getData()
                .getResult()
                .forEach(new Consumer<PromSensor>() {
                    @Override
                    public void accept(final PromSensor ps) {
                        log.debug("XX sensor: {}, {}",
                                ps.getMetric().getSensor(),
                                ps.getMetric().getJob());

                        ps.getValues().forEach(new Consumer<PromValue>() {
                            @Override
                            public void accept(final PromValue pv) {
                                log.debug("XX values: {}, {}",
                                        PromClientTests.this.jdf.format(
                                                new Date(pv.getTimestamp()
                                                        * 1000L)),
                                        Float.valueOf(pv.getValue()));

                            }
                        });
                    }
                });

        for (final PromSensor ps : promDataMatrix.getData()
                .getResult()) {
            log.debug("sensor: {}, {}", ps.getMetric().getSensor(),
                    ps.getMetric().getJob());

            for (final PromValue values : ps.getValues()) {
                log.debug("values: {}, {}",
                        this.jdf.format(
                                new Date(values.getTimestamp() * 1000L)),
                        Float.valueOf(values.getValue()));
            }

            maxValues.add(Collections.max(ps.getValues()));
            minValues.add(Collections.min(ps.getValues()));
        }

        log.debug("max: {}:", Collections.max(maxValues));
        log.debug("min: {}", Collections.min(minValues));

    }

    @Test
    void testTemperatureInstant() throws IOException, InterruptedException {
        final HttpClientPrometheus client = new HttpClientPrometheus();

        final long time = Instant.now().getEpochSecond();

        final String query = "query?query=temperature";

        final String params = String.format("time=%s", Long.toString(time));

        final HttpResponse<String> response = client.sendAndReceive(query,
                params);

        log.debug("response body: {}", response.body());

        final Gson gson = new Gson();

        final PromDataVector promDataVector = gson.fromJson(response.body(),
                PromDataVector.class);
        log.debug("promData: {}", promDataVector);

        for (final PromDataVector.PromSensor ps : promDataVector.getData()
                .getResult()) {
            log.debug("sensor: {}, {}", ps.getMetric().getSensor(),
                    ps.getMetric().getJob());

            final PromValue value = ps.getValue();
            log.debug("values: {}, {}",
                    this.jdf.format(new Date(value.getTimestamp() * 1000L)),
                    Float.valueOf(value.getValue()));
        }
    }

//    @Test
//    @Disabled
//    void testUp() throws IOException, InterruptedException {
//        final HttpClientPrometheus client = new HttpClientPrometheus();
//
//        final String upQuery2 = "query?query=up";
//
//        final HttpResponse<String> response = client.sendAndReceive(upQuery2);
//
//        log.debug("status: {}", Integer.valueOf(response.statusCode()));
//        log.debug("response body: {}", response.body());
//
//        final Gson gson = new Gson();
//
//        final PromDataMatrix target = gson.fromJson(response.body(),
//                PromDataMatrix.class);
//
//        log.debug("target: {}", target);
//        log.debug("data: {}", target.getData());
//    }

//    @Test
//    @Disabled
//    void testLabels() throws IOException, InterruptedException {
//        final HttpClientPrometheus client = new HttpClientPrometheus();
//
//        final String labelsQuery = "labels";
//
//        final HttpResponse<String> response = client
//                .sendAndReceive(labelsQuery);
//
//        log.debug("status: " + response.statusCode());
//        log.debug("response body: " + response.body());
//
//        final Gson gson = new Gson();
//
//        final PromDataMatrix target = gson.fromJson(response.body(),
//                PromDataMatrix.class);
//
//        log.debug("target: " + target);
//        log.debug("data: " + target.getData());
//    }
}
