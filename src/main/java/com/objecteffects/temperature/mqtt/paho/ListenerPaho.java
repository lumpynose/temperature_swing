package com.objecteffects.temperature.mqtt.paho;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttSubscription;

import com.objecteffects.temperature.main.Configuration;

public class ListenerPaho {
    private final static Logger log = LogManager.getLogger(ListenerPaho.class);

    private final static int qos = 2;
    private final static MemoryPersistence persistence =
            new MemoryPersistence();

    private static MqttClient client;

    public static void connect(final String broker) throws MqttException {
        try {
            log.debug("Connecting to MQTT broker: {}", broker);

            final String clientId = UUID.randomUUID().toString();

            client = new MqttClient(broker, clientId, persistence);

            final MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setAutomaticReconnect(true);

            client.connect(connOpts);

            log.debug("Connected");
        }
        catch (final MqttException me) {
            log.debug("reason: {}", Integer.valueOf(me.getReasonCode()));
            log.debug("msg: {}", me.getMessage());
            log.debug("loc: {}", me.getLocalizedMessage());
            log.debug("cause: {}", me.getCause());
            log.debug("excep: {}", me);

            log.warn(me);

            throw me;
        }

        client.setCallback(new CallbacksPaho());
    }

    public static void listen(final String topic) throws Exception {
        try {
            log.debug("Subscribing to topic: {}", topic);

            final MqttSubscription sub = new MqttSubscription(topic, qos);

            final IMqttToken token = client
                    .subscribe(new MqttSubscription[] { sub });

            log.debug("token: {}", token.getResponse());
        }
        catch (final Exception ex) {
            log.warn(ex);

            throw ex;
        }
    }

    public static void startMqttListener() {
        try {
            ListenerPaho.connect(Configuration.getBrokerAddress());

            for (final String topic : Configuration.getTopics()) {
                ListenerPaho.listen(topic);
            }
        }
        catch (final Exception ex) {
            log.warn("connect", ex);

            throw new RuntimeException(ex);
        }

        log.debug("listener started");
    }

    public static MqttClient getClient() {
        return client;
    }
}
