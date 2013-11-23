package com.example.proxy;

import org.apache.camel.EndpointConfiguration;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfBinding;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.DataFormat;
import org.apache.camel.component.cxf.DefaultCxfBinding;
import org.apache.camel.impl.UriEndpointConfiguration;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.log4j.Logger;

import java.net.URI;

/**
 * Build a proxy route between a rsServer and wsClient with wsdl stubs.
 */
public class ProxyRouteBuilder extends RouteBuilder {
    private static Logger logger = Logger.getLogger(ProxyRouteBuilder.class);
    final String uriString = "http://www.webservicex.net/stockquote.asmx";
    @Override
    public void configure() throws Exception {
        CxfEndpoint endpoint = new CxfEndpoint() {
            @Override
            public String createEndpointUri() {
                logger.info("uri");
                return uriString;
            }
        };
//        DefaultCxfBinding cxfBinding = new DefaultCxfBinding();
//        endpoint.setCxfBinding(cxfBinding);

        endpoint.setCamelContext(getContext());
        endpoint.setServiceClass(net.webservicex.StockQuoteSoap.class);
        endpoint.setAddress("http://www.webservicex.net/stockquote.asmx");
        endpoint.setDataFormat(DataFormat.POJO);

        MyProcess proxyProcessor = new MyProcess();

//        from("file:../temp?noop=true").
        from("cxfrs:bean:rsStockQuoteServer").
                to("log:input1").
                process(proxyProcessor).
                to("log:input2").
                to(endpoint).
                to("log:input3");
    }
}
