package com.example.eclinic001.configuration.sercuityConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;


@Configuration
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
    public SessionConfig() {
        super(SessionConfig.class);
    }




}
