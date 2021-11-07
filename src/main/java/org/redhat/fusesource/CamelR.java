package org.redhat.fusesource;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class CamelR extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        SimpleAggregator aggregatorStrategy = new SimpleAggregator();
        FileAggregator fileAggregator = new FileAggregator();

        restConfiguration().component("undertow").host("localhost").port(9092).bindingMode(RestBindingMode.auto);

        rest("/say")
                .post("/enricher").consumes("application/json").produces("application/json").to("direct:startEnricher")
                .post("/pollEnricher").to("direct:startPollEnricher");

        from("direct:startEnricher")
                .enrich("direct:resource", aggregatorStrategy);

        from("direct:resource")
                .transform().constant(" World");

        from("direct:startPollEnricher")
                .pollEnrich("file:/home/shchavan/development/inbox?fileName=data.txt&noop=true", 5000, fileAggregator);
    }
}
