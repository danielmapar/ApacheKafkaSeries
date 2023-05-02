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