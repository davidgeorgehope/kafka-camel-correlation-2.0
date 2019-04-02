# AppDynamics Kafka Camel Correlation

## Use Case
Enables AppDynamics correlation from Camel to Kafka to consumers 

## Installation

1. Copy https://github.com/appdynamicsdh/kafka-camel-correlation-2.0/blob/master/target/kafka-camel-correlation-2.0-SNAPSHOT.jar to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins
2. Add -Dallow.unsigned.sdk.extension.jars=true to the java agent command line.
3. Restart the Java Agent process.
4. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).

## To add this to a running Docker container

Copy https://github.com/appdynamicsdh/kafka-camel-correlation/blob/master/kafka-camel-correlation-1.0-SNAPSHOT.jar inside the container 

1. docker cp kafka-camel-correlation.jar {instanceId}:/opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins
2. docker exec -it {instanceId} /bin/sh and add -Dallow.unsigned.sdk.extension.jars=true to the java command line. 
3. docker stop {instanceId}
4. docker start {instanceId}
5. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).


![Correlation Screenshot](https://github.com/appdynamicsdh/kafka-camel-correlation/KafkaCamelCorrelation.png)
