package org.redhat.fusesource;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class SimpleAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        String originalBody = (String) oldExchange.getIn().getBody();
        String newBody = (String) newExchange.getIn().getBody();
        String mergedResult = originalBody + newBody;
        if(oldExchange.getPattern().isOutCapable()){
            oldExchange.getOut().setBody(mergedResult);
        }else {
            oldExchange.getIn().setBody(mergedResult);
        }
        return oldExchange;
    }
}
