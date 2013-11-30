package com.example.proxy;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class StockQuoteRouteBuilderTest extends CamelTestSupport {

    @Override
    public RouteBuilder[] createRouteBuilders() throws Exception {
        // stub producer endpoint, otherwise configured in XML
        Endpoint stub = new ProcessorEndpoint("bean:clientStub", context(), new StockQuoteClientStub());
        context().addEndpoint("bean:clientStub", stub);
        return new RouteBuilder[] {
            new StockQuoteProxyRouteBuilder("direct:server", "mock:client"),
            new StockQuoteProxyRouteBuilder("direct:serverStub", "bean:clientStub")
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

    /**
     * Here we use the second route that has the stub in place of the ws client endpoint.
     * This should probably be a separate test class, and just test the stub itself.
     * N.B. The correct endpoint configuration should be tested in ISO and FUSION, not in unit test.
     */
    @Test
    public void shouldRespondWithStub() {
        Object result = template.requestBody("direct:serverStub", "AAPL");
        assertEquals("<StockQuotes><Stock><Symbol>GPS</Symbol><Last>41.31</Last><Date>11/22/2013</Date><Time>4:01pm</Time><Change>-0.55</Change><Open>41.49</Open><High>41.68</High><Low>40.36</Low><Volume>7502664</Volume><MktCap>19.127B</MktCap><PreviousClose>41.86</PreviousClose><PercentageChange>-1.31%</PercentageChange><AnnRange>29.84 - 46.56</AnnRange><Earns>2.804</Earns><P-E>14.93</P-E><Name>Gap</Name></Stock></StockQuotes>", result);
    }
}
