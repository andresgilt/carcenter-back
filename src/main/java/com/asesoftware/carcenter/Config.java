package com.asesoftware.carcenter;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Arrays;


@Configuration

public class Config {

    @Bean
    public Module hibernate5Module() {
        Hibernate5Module module = new Hibernate5Module();
        module.enable(Feature.FORCE_LAZY_LOADING);
        module.disable(Feature.USE_TRANSIENT_ANNOTATION);
        return module;
    }


}
