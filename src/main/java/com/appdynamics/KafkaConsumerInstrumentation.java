package com.appdynamics;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;
import com.appdynamics.instrumentation.sdk.contexts.ISDKUserContext;
import com.appdynamics.instrumentation.sdk.template.AEntry;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;

import java.util.ArrayList;
import java.util.List;

public class KafkaConsumerInstrumentation extends AEntry {

    //getIn().getHeader(kafka.HEADERS).lastHeader(singularityheader).value
    private static final String CLASS_TO_INSTRUMENT = "org.apache.camel.processor.DelegateAsyncProcessor";
    private static final String METHOD_TO_INSTRUMENT = "process";
    private IReflector key = null;
    private IReflector value = null;
    private IReflector getHeader = null;
    private IReflector getIn = null;
    private IReflector lastHeader = null;

    private IReflector getHeaders = null;
    private boolean identifyBt = true;

    public KafkaConsumerInstrumentation() {
        super();
        boolean searchSuperClass = true;

        getHeaders = getNewReflectionBuilder()
                .invokeInstanceMethod("headers", searchSuperClass)
                .build();

        key = getNewReflectionBuilder()
                .invokeInstanceMethod("key", searchSuperClass)
                .build();

        value = getNewReflectionBuilder()
                .invokeInstanceMethod("value", searchSuperClass)
                .build();

        this.getIn = this.getNewReflectionBuilder().invokeInstanceMethod("getIn", searchSuperClass).build();

        String[] types = new String[]{String.class.getCanonicalName()};
        this.getHeader = this.getNewReflectionBuilder().invokeInstanceMethod("getHeader", searchSuperClass, types).build();
        this.lastHeader = this.getNewReflectionBuilder().invokeInstanceMethod("lastHeader", searchSuperClass, types).build();


    }

    @Override
    public List<Rule> initializeRules() {
        List<Rule> result = new ArrayList<>();

        Rule.Builder bldr = new Rule.Builder(CLASS_TO_INSTRUMENT);
        bldr = bldr.classMatchType(SDKClassMatchType.MATCHES_CLASS).classStringMatchType(SDKStringMatchType.EQUALS);
        bldr = bldr.methodMatchString(METHOD_TO_INSTRUMENT).methodStringMatchType(SDKStringMatchType.EQUALS);
        result.add(bldr.build());
        return result;
    }

    //getIn().getHeader(kafka.HEADERS).lastHeader(singularityheader).value


    @Override
    public String unmarshalTransactionContext(Object invokedObject, String className, String methodName,
                                              Object[] paramValues, ISDKUserContext context) throws ReflectorException {
      String result = null;

        try {
            if (paramValues != null && paramValues.length > 0) {
                Object exchange = paramValues[0];
                if (exchange != null) {
                    try {
                        //getIn()

                        Object in = this.getIn.execute(exchange.getClass().getClassLoader(), exchange);
                        byte[] body = null;

                        //.getHeader(kafka.HEADERS)
                        Object header =this.getHeader.execute(in.getClass().getClassLoader(), in, new Object[]{"kafka.HEADERS"});

                        //.lastHeader(singularityheader)
                        Object lastHeader = this.lastHeader.execute(in.getClass().getClassLoader(), header, new Object[]{ITransactionDemarcator.APPDYNAMICS_TRANSACTION_CORRELATION_HEADER});

                        //.value
                        body = this.value.execute(in.getClass().getClassLoader(), lastHeader);
                        result = new String(body);

                    } catch (Exception e) {
                        getLogger().info("Error",e);
                    }
                }
            }
        } catch (Exception f) {
            getLogger().info("Error",f);
        }
        return result;
    }

    @Override
    public String getBusinessTransactionName(Object invokedObject, String className,
                                             String methodName, Object[] paramValues, ISDKUserContext context) throws ReflectorException {
        String result = null;
        if (identifyBt)
            result = new String("Kafka Receive");
        return result;
    }

    @Override
    public boolean isCorrelationEnabled() {
        return true;
    }

    @Override
    public boolean isCorrelationEnabledForOnMethodBegin() {
        return true;
    }
}