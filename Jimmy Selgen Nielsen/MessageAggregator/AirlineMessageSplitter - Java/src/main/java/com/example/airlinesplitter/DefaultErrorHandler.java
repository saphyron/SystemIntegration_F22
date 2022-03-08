package com.example.airlinesplitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class DefaultErrorHandler implements ErrorHandler {


    private static final Logger log = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        log.warn("spring jms custom error handling example");
        log.error(t.getCause().getMessage());
    }
}
