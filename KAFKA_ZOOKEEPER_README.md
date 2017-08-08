# Admin Commands

[Zookeeper documentation](https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html#sc_zkCommands)

Kafka:
* [Instructions from Docker Image maintenainer](http://wurstmeister.github.io/kafka-docker/)
* [DockerHub](https://hub.docker.com/r/wurstmeister/kafka/)

#### Add KAFKA_HOST_PORT environ variable read by by producer.py and aliases to check if Zookeeper is running

```
export KAFKA_HOST_PORT=localhost:9092

alias zoostat="echo stat | nc 127.0.0.1 2181"
alias zoomntr="echo mntr | nc 127.0.0.1 2181"
alias zooenvi="echo envi | nc 127.0.0.1 2181"
```

#### Connect to running Kafka Zookeper Docker Container

```
$ docker exec -i -t ${ZOOKEPER_CONTAINER ID} bash
$ docker exec -i -t 3300a432b299 bash
```

#### Connecting to Kafka Shell

```
$ docker exec -i -t ${KAFKA_CONTAINER ID} bash
$ docker exec -i -t a3282cd983c8 bash
$ start-kafka-shell.sh <DOCKER_HOST_IP> <ZK_HOST:ZK_PORT>
$ start-kafka-shell.sh 127.0.0.1 zookeeper:2181
```

## Topic Info

```
./kafka-topics.sh --describe --zookeeper zookeeper:2181 --topic topic_name
```

## Verify that Zookeeper is running

```
$ echo stat | nc 192.168.99.100 2181
$ echo stat | nc 127.0.0.1 2181
```

Result
```
Zookeeper version: 3.4.9-1757313, built on 08/23/2016 06:50 GMT
Clients:
 /172.23.0.1:57526[0](queued=0,recved=1,sent=0)
 /172.23.0.3:52344[1](queued=0,recved=1440,sent=1444)
 /172.23.0.4:57548[1](queued=0,recved=224,sent=224)

Latency min/avg/max: 0/0/42
Received: 1685
Sent: 1688
Connections: 3
Outstanding: 0
Zxid: 0x38
Mode: standalone
Node count: 36
```

```
$ echo mntr | nc 192.168.99.100 2181
$ echo mntr | nc 127.0.0.1 2181
```

Result

```
zk_version	3.4.9-1757313, built on 08/23/2016 06:50 GMT
zk_avg_latency	0
zk_max_latency	42
zk_min_latency	0
zk_packets_received	1759
zk_packets_sent	1762
zk_num_alive_connections	3
zk_outstanding_requests	0
zk_server_state	standalone
zk_znode_count	36
zk_watch_count	17
zk_ephemerals_count	2
zk_approximate_data_size	1358
zk_open_file_descriptor_count	30
zk_max_file_descriptor_count	1048576
```

## Get Zookeeper Environment Information

```
$ echo envi | nc 192.168.99.100 2181
$ echo envi | nc 127.0.0.1 2181
```

Result

```
Environment:
zookeeper.version=3.4.6-1569965, built on 02/20/2014 09:09 GMT
host.name=7167a11e1223
java.version=1.7.0_65
java.vendor=Oracle Corporation
java.home=/usr/lib/jvm/java-7-openjdk-amd64/jre
java.class.path=/opt/zookeeper-3.4.6/bin/../build/classes:/opt/zookeeper-3.4.6/bin/../build/lib/*.jar:/opt/zookeeper-3.4.6/bin/../lib/slf4j-log4j12-1.6.1.jar:/opt/zookeeper-3.4.6/bin/../lib/slf4j-api-1.6.1.jar:/opt/zookeeper-3.4.6/bin/../lib/netty-3.7.0.Final.jar:/opt/zookeeper-3.4.6/bin/../lib/log4j-1.2.16.jar:/opt/zookeeper-3.4.6/bin/../lib/jline-0.9.94.jar:/opt/zookeeper-3.4.6/bin/../zookeeper-3.4.6.jar:/opt/zookeeper-3.4.6/bin/../src/java/lib/*.jar:/opt/zookeeper-3.4.6/bin/../conf:
java.library.path=/usr/java/packages/lib/amd64:/usr/lib/x86_64-linux-gnu/jni:/lib/x86_64-linux-gnu:/usr/lib/x86_64-linux-gnu:/usr/lib/jni:/lib:/usr/lib
java.io.tmpdir=/tmp
java.compiler=<NA>
os.name=Linux
os.arch=amd64
os.version=4.1.17-boot2docker
user.name=root
user.home=/root
user.dir=/opt/zookeeper-3.4.6
```


## Docker Utility Commands

### Connect to running Docker Container

```
$ docker exec -i -t ${CONTAINER ID} bash
```

### To start a cluster with two brokers

```
$ docker-compose scale kafka=2
```

### Stopping all running containers

```
$ docker stop $(docker ps -a -q)
```

### Removing containers `docker rm`, images `docker rmi` and volumes `docker volume rm`


```
$ docker stop $(docker ps -a -q)
$ docker rm $(docker ps -a -q)
$ docker rmi $(docker images -q)
$ docker ps -a | grep "pattern" | awk '{print $3}' | xargs docker rmi
$ docker rm -v container_name
```

### When Docker fails to start

```
docker-machine rm default
```
