# AppDynamics Kafka Camel Correlation

## Use Case
Enables AppDynamics correlation from Camel to Kafka to consumers 

## Installation

1. Copy https://github.com/appdynamicsdh/kafka-camel-correlation-2.0/blob/master/target/kafka-camel-correlation-2.0-SNAPSHOT.jar to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins on any consumer nodes.
2. Add -Dallow.unsigned.sdk.extension.jars=true to the java agent command line.
3. Restart the Java Agent process.
4. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).

![Correlation Screenshot](https://github.com/appdynamicsdh/kafka-camel-correlation-2.0/blob/master/KafkaCamelCorrelation.png)
