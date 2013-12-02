package com.example.proxy;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Build a proxy route between a rsServer and wsClient.
 */
public class StockQuoteProxyRouteBuilder extends RouteBuilder {

    public static final String SERVER_ENDPOINT = "cxfrs:bean:rsStockQuoteServer";
    public static final String CLIENT_ENDPOINT = "cxf:bean:wsStockQuoteClient";

    private String serverEndpoint;
    private String clientEndpoint;

    public StockQuoteProxyRouteBuilder() {
       this(SERVER_ENDPOINT, CLIENT_ENDPOINT);
    }

    /**
     * This constructor allows for testing via endpoint test doubles.
     *
     * @param serverEndpoint
     * @param clientEndpoint
     */
    public StockQuoteProxyRouteBuilder(String serverEndpoint, String clientEndpoint) {
        super();
        this.serverEndpoint = serverEndpoint;
        this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void configure() throws Exception {
        // convert client exceptions to server exceptions, wrapped in 503
        // but this ought to be 502 or 504, since we're a gateway
        // 503 should be from the remote service
        // superclass of UnknownHostException, NoRouteToHostException, etc
        onException(IOException.class).
            handled(true).
            process(new Processor() {
                @Override
                public void process(Exchange exchange) {
                    // need to ask for Exception in general, even for EXCEPTION_CAUGHT
                    Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    String message = exception.getCause().getMessage();
                    Response response = Response.status(503).entity(message).build();
                    exchange.getOut().setBody(response);
                }
            });

        from(serverEndpoint).
            to("log:input1").
            process(new StockQuoteRequestProcessor()).
            to("log:input2").
            to(clientEndpoint).
            to("log:input3");
    }
}
