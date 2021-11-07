package org.redhat.fusesource;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Slf4j
public class FileAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        log.info("New Exchange data: {}", newExchange.getIn().getBody().toString());
        GenericFile dataFile = (GenericFile) newExchange.getIn().getBody();
        try {
            log.info("FileName: {}", dataFile.getAbsoluteFilePath());
            File tmpFile = new File(dataFile.getAbsoluteFilePath());
            Scanner sc =    new Scanner(tmpFile);
            sc.useDelimiter("\\Z");
            String oldData = (String) oldExchange.getIn().getBody();
            String mergedResult = oldData + sc.next();
            log.info("Final Result: {}", mergedResult);
            if(oldExchange.getPattern().isOutCapable()){
                oldExchange.getOut().setBody(mergedResult);
            }else {
                oldExchange.getIn().setBody(mergedResult);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return oldExchange;
    }
}
