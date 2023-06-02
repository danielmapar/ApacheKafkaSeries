# Apache Kafka Series

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
