package com.example.proxy;


import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service("localService")
@Produces({ MediaType.APPLICATION_JSON })

public class LocalService {
    @Path("symbol/{symbol}")
    @GET
    public
    String getQuote(@PathParam("symbol") String symbol) {
        return symbol;
    }
}