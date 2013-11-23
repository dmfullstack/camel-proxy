package com.example.proxy;


import net.webservicex.GetQuote;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.camel.component.cxf.common.message.CxfConstants.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyProcess implements org.apache.camel.Processor {
    private static Logger logger = Logger.getLogger(MyProcess.class);

    @Override
    public void process(Exchange exchange) throws Exception {
//        logger.info(" cxf:bean:releasePOEndpoint processing exchange in camel");

        BindingOperationInfo boi = (BindingOperationInfo)
                exchange.getProperty(BindingOperationInfo.class.toString());
        if (boi != null) {
            logger.info("boi.isUnwrapped" + boi.isUnwrapped());
            logger.info("boi: " + boi);
        }
        if (exchange != null){
            Object symbol = exchange.getIn().getBody();
            logger.info("symbol: " + symbol);
            exchange.getOut().setHeader(CxfConstants.OPERATION_NAMESPACE, "http://www.webserviceX.NET/");
            exchange.getOut().setHeader(CxfConstants.OPERATION_NAME, "GetQuote");
            exchange.getOut().setHeader("SOAPAction", "http://www.webserviceX.NET/GetQuote");
            exchange.getOut().setBody(symbol);
        }
    }
}
