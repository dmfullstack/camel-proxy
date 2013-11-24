package com.example.proxy;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.Future;

public class StockQuoteProxyTest extends CamelTestSupport {

    @Override
    public RouteBuilder[] createRouteBuilders() {
        return new RouteBuilder[] {
            new ProxyRouteBuilder("direct:server", "mock:client"),
            new ProxyRouteBuilder("direct:serverR", "mock:clientR")
        };
    }

    /**
     * Here we just verify that the processor passed the parameter to the
     * producer.
     */
    @Test
    public void shouldMarshalSoapRequest() {
        template.sendBody("direct:server", "GPS");
        MockEndpoint mock = getMockEndpoint("mock:client");
        assertEquals(1, mock.getExchanges().size());
        Object mockBody = mock.getExchanges().get(0).getIn().getBody();
        assertEquals("GPS", mockBody);
    }

    /**
     * Here we use mock.returnReplyBody to make the mock return a canned response
     * and use template.requestBody to verify the response the server gets.
     */
    @Test
    public void shouldRoundTrip() {
        MockEndpoint mock = getMockEndpoint("mock:client");
        mock.returnReplyBody(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {
                String symbol = (String) exchange.getIn().getBody();
                return (T) ("<StockQuote>" + symbol + "</StockQuote>");
            }
        });
        Object result = template.requestBody("direct:server", "AAPL");
        assertEquals("<StockQuote>AAPL</StockQuote>", result);
    }
}
