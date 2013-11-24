package com.example.proxy;


import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.log4j.Logger;

/**
 * In order to invoke the ws client, the out message of the exchange
 * needs the correct headers. The parameter also needs to be added.
 */
public class MyProcess implements org.apache.camel.Processor {
    private static Logger logger = Logger.getLogger(MyProcess.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange != null) {
            Object symbol = exchange.getIn().getBody();
            logger.info("symbol: " + symbol);
            exchange.getOut().setHeader(CxfConstants.OPERATION_NAMESPACE, "http://www.webserviceX.NET/");
            exchange.getOut().setHeader(CxfConstants.OPERATION_NAME, "GetQuote");
            exchange.getOut().setHeader("SOAPAction", "http://www.webserviceX.NET/GetQuote");
            exchange.getOut().setBody(symbol);
        }
    }
}
