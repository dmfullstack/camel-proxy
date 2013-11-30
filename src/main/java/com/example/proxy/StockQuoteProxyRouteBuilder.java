package com.example.proxy;

import org.apache.camel.builder.RouteBuilder;

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
        from(serverEndpoint).
            to("log:input1").
//            to("bean:requestProcessor").
                process(new StockQuoteRequestProcessor()).

                to("log:input2").
            to(clientEndpoint).
            to("log:input3");
    }
}
