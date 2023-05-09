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
    * `docker-compose exec kafka /bin/sh`
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