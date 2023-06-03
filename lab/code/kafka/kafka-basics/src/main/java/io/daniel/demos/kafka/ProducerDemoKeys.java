package io.daniel.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());
    public static void main(String[] args) {
        log.info("I am a Kafka producer!");

        // create the Producer properties

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        // set producer properties

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // create the Producer
        // the key is of type string and so is the value
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "demo_java_2";

        for (int i = 0; i < 10; i++) {
            String key = "id_" + i;
            String value = "hello world new " + i;

            // create a producer records
            // the key is of type string and so is the value
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topic, key, value);

            // send data
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e == null) {
                        // the record was successfully sent
                        log.info("Receive new metadata \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Key: " + key + ", Partition: " + metadata.partition() + "\n" +
                                "Offset: " + metadata.offset() + "\n" +
                                "Timestamp: " + metadata.timestamp() + "\n");
                    } else {
                        log.error("Error while producing", e);
                    }
                }
            });
        }

        // tell the producer to send all data and block until done - synchronous
        producer.flush();

        // flush and close the producer
        producer.close();
    }
}
