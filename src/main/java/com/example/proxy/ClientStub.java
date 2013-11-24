package com.example.proxy;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.bean.BeanEndpoint;

/**
 * Created with IntelliJ IDEA.
 * User: webpos
 * Date: 11/24/13
 * Time: 1:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientStub implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getOut().setBody("<StockQuotes><Stock><Symbol>GPS</Symbol><Last>41.31</Last><Date>11/22/2013</Date><Time>4:01pm</Time><Change>-0.55</Change><Open>41.49</Open><High>41.68</High><Low>40.36</Low><Volume>7502664</Volume><MktCap>19.127B</MktCap><PreviousClose>41.86</PreviousClose><PercentageChange>-1.31%</PercentageChange><AnnRange>29.84 - 46.56</AnnRange><Earns>2.804</Earns><P-E>14.93</P-E><Name>Gap</Name></Stock></StockQuotes>");
    }
}
