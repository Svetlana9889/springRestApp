package com.example.springresttask.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperService {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}