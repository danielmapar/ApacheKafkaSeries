# Apache Kafka Series

* Manual link: https://www.conduktor.io/kafka/what-is-apache-kafka/

## Kafka Theory

* Topics: 
    * ![kafka_topics](./images/kafka_topics_1.png)

* Partitions and offsets:
    * ![partitions](./images/partitions_1.png)
    * ![partitions](./images/partitions_2.png)
    * ![partitions](./images/partitions_3.png)

* Producers and Message Keys:
    * ![producers](./images/producers_1.png)
    * ![producers](./images/producers_2.png)
    * ![producers](./images/producers_3.png)

* Kafka Message Serialization:
    * ![serialize](./images/serialize_1.png)
    * ![serialize](./images/serialize_2.png)
        * MurmurHash is a non-cryptographic hash function suitable for general hash-based lookup.[1][2][3] It was created by Austin Appleby in 2008[4] and is currently hosted on GitHub along with its test suite named 'SMHasher'. It also exists in a number of variants,[5] all of which have been released into the public domain. The name comes from two basic operations, multiply (MU) and rotate (R), used in its inner loop.

        * Unlike cryptographic hash functions, it is not specifically designed to be difficult to reverse by an adversary, making it unsuitable for cryptographic purposes.

* Consumers:
    * ![consumers](./images/consumers_1.png)
    * ![consumers](./images/consumers_2.png)

* Consumer Groups & Consumer Offsets:
    * ![consumer_group](./images/consumer_group_1.png)
    * ![consumer_group](./images/consumer_group_2.png)
    * ![consumer_group](./images/consumer_group_3.png)
    * ![consumer_group](./images/consumer_group_4.png)

* Delivery semantics for consumers:
    * ![consumer_group](./images/consumer_group_5.png)
        * Consumer offset is stored and tracked separately for each topic-partition, so consumer instances when assigned a partition know exactly from which offset to start reading. Also, of course, offsets are stored and tracked for each consumer group separately.

* Kafka Brokers
    * ![broker](./images/broker_1.png)
    * ![broker](./images/broker_2.png)
    * ![broker](./images/broker_3.png)

* Topic replication factor
    * ![broker](./images/broker_4.png)
    * ![broker](./images/broker_5.png)
    * ![broker](./images/broker_6.png)
    * ![broker](./images/broker_7.png)
        * Producer writes can only happen to the partition leader broker.
    * ![broker](./images/broker_8.png)
        * Configure consumer to closest broker (replica).

* Producer Acknowledgments and Topic Durability 
    * ![broker](./images/broker_9.png)
    * ![broker](./images/broker_10.png)

* Zookeeper
    * ![zookeeper](./images/zookeeper_1.png)
    * ![zookeeper](./images/zookeeper_2.png)
    * ![zookeeper](./images/zookeeper_3.png)
    * ![zookeeper](./images/zookeeper_4.png)
        * https://raft.github.io/
    * ![zookeeper](./images/zookeeper_5.png)
    * ![zookeeper](./images/zookeeper_5.png)

* Theory Roundup
    * ![theory_roundup](./images/theory_roundup.png)

## Setting up Kafka

* Navigate to the `lab` folder in this repo and run `docker-compose`
    * `cd lab && docker-compose up -d`

* SSH into the Kafka contained by running:
    * `docker-compose exec kafka /bin/bash`
    * `cd /opt/bitnami/kafka/bin`

* List topics inside our broker:
    * `kafka-topics.sh --list  --bootstrap-server 127.0.0.1:9092`
    * `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic test`
    * `kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test --from-beginning`
    * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --topic test --describe`

* Important note:
    * ![kafka_cli](./images/kafka_cli_1.png)

* Kafka Topic CLI:
    * Create Kafka Topics
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --topic first_topic`
            * We are not defining the number of partitions 
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --topic second_topic --partitions 5`
            * This topic contains 5 partitions
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --topic third_topic --partitions 5 --replication-factor 2`
            * The target replication factor of 2 cannot be reached because only 1 broker(s) are registered. Remember, replication is tied to number of brokers
    * List Kafka Topics
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --list`
    * Describre Kafka Topics
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --describe`
    * Increase Partitions in a Kafka Topic
        * If you want to change the number of partitions or replicas of your Kafka topic, you can use a streaming transformation to automatically stream all of the messages from the original topic into a new Kafka topic that has the desired number of partitions or replicas.
    * Delete a Kafka Topic
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --delete`

* Kafka Console Producer CLI

    * ![kafka_producer_cli](./images/kafka_producer_cli_1.png)
        * Producer without keys: It will distribute values using the Robin Hood approach
        * Producer using keys: It will distribute on the key hash (`murmur2`)

    * Create a topic with one partition:
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --topic producer_topic --partitions 1`

    * Produce messages to the topic:
        * `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic producer_topic`
            * ```bash
                >Hello World
                >My name is Daniel
                >I love Kafka
                > Control + C
                ```

    * Produce messages to the topic with extra properties:
        * `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic producer_topic --producer-property acks=all`

    * Produce messages to a topic that does not exist:
        * `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic producer_topic_new`
            * `[2023-05-31 19:03:57,926] WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 4 : {producer_topic_new=UNKNOWN_TOPIC_OR_PARTITION} (org.apache.kafka.clients.NetworkClient)`
            * It will fail, but it will create the topic nontheless. - auto creationg
            * You can edit `config/server.properties` to set the default number of partitions.

    * Produce messages with a key: `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic producer_topic_new --property parse.key=true --property key.separator=:`
        * ```bash
            >example key:example value
            >name:daniel
            ```

        * ```bash
            kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --from-beginning --topic producer_topic_new
            > new topic
            > kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --list
            > testtest
            > example value
            > daniel
            ```

* Kafka Console Consumer CLI
    * ![kafka_consumer_cli](./images/kafka_consumer_cli_1.png)

    * Consuming messages from the beginning
        * `kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test --from-beginning`

    * Producing messages
        * `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9092 --topic producer_topic_new --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner`
            * We want to use the Round Robin partitioner to distribute keys equaly between patitions. If we don't use, Kafka will probably send the data to the same partition.

    * Consuming messages with key
        * `kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic producer_topic_new --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --from-beginning`

* Kafka Consumers in Groups

    * ![kafka_consumer_group_cli](./images/kafka_consumer_group_cli_1.png)

    * Create a topic
        * `kafka-topics.sh --bootstrap-server 127.0.0.1:9092 --create --topic group_topic --partitions 5`

    * Consume the topic from a group
        * `kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic group_topic --group my-first-application`
            * If we create multiple consumers using the same group, each consumer will take care of some unique partitions. e.g. consumer 1 takes care of partition 0 and consumer 2 takes care of partition 1.

        * ![kafka_consumer_group_cli](./images/kafka_consumer_group_cli_2.png)

        * `kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic group_topic --group my-first-application --from-beginning`
            * The group will ignore the `--from-beginning` flag if he already read some messages (commited an offset)

* Consumer Group Managment CLI

    * ![kafka_consumer_group_cli](./images/kafka_consumer_group_cli_3.png)

    * List consumer groups
        * `kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --list`

    * Describe group
        * `kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group my-first-application --describe`

        * ![kafka_consumer_group_cli](./images/kafka_consumer_group_cli_4.png)
            * Lag = current offset - consumer offset
            * Each consumer inside a consumer group has an id

* Consumer Group - Reset Offset

    * ![kafka_consumer_group_cli](./images/kafka_consumer_group_cli_5.png)

    * Describe group
        * `kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group my-first-application --describe`

    * Reset the offset of this group to read all messages

        * `kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group my-first-application --reset-offsets --to-earliest --dry-run --topic group_topic`
            * `--dry-run` shows the changes that will be applied, but it does not apply them.

        * `kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic group_topic`
            * `--execute` applies the changes to the group offset.

* Producer - Sticky Partition

    * ![kafka_producer_sticky](./images/kafka_producer_sticky_1.png)

    * The producer may try to be efficient and send a batch of messages to the same partition.
        * `partitioner.call = null`
        * default partitioner (no key)

* Producer - Key

    * ![kafka_producer](./images/kafka_producer_10.png)

* Consumer Group - Rebalancing

    * ![consumer_group](./images/consumer_group_10.png)

    * ![consumer_group](./images/consumer_group_11.png)

    * ![consumer_group](./images/consumer_group_12.png)

    * ![consumer_group](./images/consumer_group_13.png)

    * ![consumer_group](./images/consumer_group_14.png)

    * ![consumer_group](./images/consumer_group_15.png)

    * ![consumer_group](./images/consumer_group_16.png)

    * The previous code examples should be enough for 90% of your use cases.

    * Here, we have included advanced examples for the following use cases:

    * Consumer Rebalance Listener: in case you're doing a manual commit of your offsets to Kafka or externally, this allows you to commit offsets when partitions are revoked from your consumer.

    * Consumer Seek and Assign: if instead of using consumer groups, you want to start at specific offsets and consume only specific partitions, you will learn about the .seek() and .assign() APIs.

    * Consumer in Threads: very advanced code samples, this is for those who want to run their consumer `.poll()` loop in a separate thread.

* Producer ACKs and idempotency

    * ![producers_ack_all](./images/producers_ack_all.png)
        * `acks=all walks hand in hand with min.insync.replicas`

    * ![producers_ack_all](./images/producers_ack_all_2.png)

    * ![producers_retries](./images/producer_retries_1.png)

    * ![producers_retries](./images/producer_retries_2.png)

    * ![idempotent_producer](./images/idempotent_producer_1.png)

    * ![idempotent_producer](./images/idempotent_producer_2.png)

    * ![idempotent_producer](./images/idempotent_producer_3.png)

    * ![idempotent_producer](./images/idempotent_producer_4.png)

    * ![idempotent_producer](./images/idempotent_producer_5.png)

* Message Compression

    * ![message_compression](./images/message_compression_1.png)

    * ![message_compression](./images/message_compression_2.png)

    * ![message_compression](./images/message_compression_3.png)

    * ![batch_speed](./images/batch_speed_1.png)

    * ![batch_speed](./images/batch_speed_2.png)

    * ![batch_speed](./images/batch_speed_3.png)

    * ![batch_speed](./images/batch_speed_4.png)

* Compression (snappy)

    * ![compression](./images/compression_1.png)

* Partitioner

    * ![partitioner](./images/partitioner_1.png)

    * ![partitioner](./images/partitioner_2.png)

    * ![partitioner](./images/partitioner_3.png)

    * ![partitioner](./images/partitioner_4.png)

    * ![partitioner](./images/partitioner_5.png)

* Block buffer

    * ![block](./images/block_1.png)

* Delivery Semantics
    * ![delivery_semantics](./images/delivery_semantics_1.png)

    * ![delivery_semantics](./images/delivery_semantics_2.png)

    * ![delivery_semantics](./images/delivery_semantics_3.png)

* Consumer Offset Commit Strategy
    * ![delivery_semantics](./images/delivery_semantics_4.png)

    * ![delivery_semantics](./images/delivery_semantics_5.png)

    * ![delivery_semantics](./images/delivery_semantics_6.png)

    * ![delivery_semantics](./images/delivery_semantics_7.png)

    * ![delivery_semantics](./images/delivery_semantics_8.png)

    * ![delivery_semantics](./images/delivery_semantics_9.png)

* Controlling Consumer Liveliness

    * ![delivery_semantics](./images/delivery_semantics_10.png)

    * ![delivery_semantics](./images/delivery_semantics_11.png)

    * ![delivery_semantics](./images/delivery_semantics_12.png)

    * ![delivery_semantics](./images/delivery_semantics_13.png)

    * ![delivery_semantics](./images/delivery_semantics_14.png)

    * ![delivery_semantics](./images/delivery_semantics_15.png)

## Kafka Extended APIs for Developers

* ![kafka_extended](./images/kafka_extended_1.png)

* ![kafka_streams](./images/kafka_streams_1.png)

* ![schema_registry](./images/schema_registry_1.png)

* ![schema_registry](./images/schema_registry_2.png)

* ![schema_registry](./images/schema_registry_3.png)

* ![schema_registry](./images/schema_registry_4.png)

* ![schema_registry](./images/schema_registry_5.png)

* ![schema_registry](./images/schema_registry_6.png)

## Kafka Real World Architecture

* ![kafka_apis](./images/kafka_apis_1.png)

* ![kafka_real_world](./images/kafka_real_world_1.png)

* ![kafka_real_world](./images/kafka_real_world_2.png)

* ![kafka_real_world](./images/kafka_real_world_3.png)

* ![kafka_real_world](./images/kafka_real_world_4.png)

## Topic Naming Convention

* ![kafka_real_world](./images/kafka_real_world_5.png)

* ![kafka_real_world](./images/kafka_real_world_6.png)

## Big Data Ingestion

* ![kafka_real_world](./images/kafka_real_world_7.png)

* ![kafka_real_world](./images/kafka_real_world_8.png)

## Kafka in the Enterprise for Admins

* ![kafka_real_world](./images/kafka_real_world_9.png)

* ![kafka_real_world](./images/kafka_real_world_10.png)

* ![kafka_real_world](./images/kafka_real_world_11.png)

* ![kafka_real_world](./images/kafka_real_world_12.png)

* ![kafka_real_world](./images/kafka_real_world_13.png)

* ![kafka_real_world](./images/kafka_real_world_14.png)

* ![kafka_real_world](./images/kafka_real_world_15.png)

* ![kafka_real_world](./images/kafka_real_world_16.png)

* ![kafka_real_world](./images/kafka_real_world_17.png)

* ![kafka_real_world](./images/kafka_real_world_18.png)

* ![kafka_real_world](./images/kafka_real_world_19.png)

* ![kafka_real_world](./images/kafka_real_world_20.png)

* ![kafka_real_world](./images/kafka_real_world_21.png)

* ![kafka_real_world](./images/kafka_real_world_22.png)

* ![kafka_real_world](./images/kafka_real_world_23.png)

* ![kafka_real_world](./images/kafka_real_world_24.png)

* ![kafka_real_world](./images/kafka_real_world_25.png)

* ![kafka_real_world](./images/kafka_real_world_26.png)

* ![kafka_real_world](./images/kafka_real_world_27.png)

## Advanced Topics Configuration

* ![advanced_topics](./images/advanced_topics_1.png)

* ![advanced_topics](./images/advanced_topics_2.png)

* ![advanced_topics](./images/advanced_topics_3.png)

* ![advanced_topics](./images/advanced_topics_4.png)

* ![advanced_topics](./images/advanced_topics_5.png)

* ![advanced_topics](./images/advanced_topics_6.png)

* ![advanced_topics](./images/advanced_topics_7.png)

* ![advanced_topics](./images/advanced_topics_8.png)

* ![advanced_topics](./images/advanced_topics_9.png)

* ![advanced_topics](./images/advanced_topics_10.png)

* ![advanced_topics](./images/advanced_topics_11.png)

* ![advanced_topics](./images/advanced_topics_12.png)

* ![advanced_topics](./images/advanced_topics_13.png)

* ![advanced_topics](./images/advanced_topics_14.png)

* ![advanced_topics](./images/advanced_topics_15.png)

* ![advanced_topics](./images/advanced_topics_16.png)

* ![advanced_topics](./images/advanced_topics_17.png)

* ![advanced_topics](./images/advanced_topics_18.png)

* ![advanced_topics](./images/advanced_topics_19.png)

* ![advanced_topics](./images/advanced_topics_20.png)

* ![advanced_topics](./images/advanced_topics_21.png)