package com.example.proxy;

import net.webservicex.GetQuote;

/**
 * Created with IntelliJ IDEA.
 * User: webpos
 * Date: 11/18/13
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class FakeConvertor {
    public GetQuote convert(String s) {
        GetQuote gq = new GetQuote();
        gq.setSymbol("GPS");
        return gq;
    }
}
