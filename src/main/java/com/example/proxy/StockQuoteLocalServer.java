package com.example.proxy;


import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A simple REST stock quote server
 */
@Service("localService")
@Produces({ MediaType.APPLICATION_JSON })
public class StockQuoteLocalServer {

    @Path("symbol/{symbol}")
    @GET
    public String getQuote(@PathParam("symbol") String symbol) {
        return symbol;
    }

}
