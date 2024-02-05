package com.example.techblogplatformapi.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;



@Configuration
public class MongoConfiguration {

    @Autowired
    private MappingMongoConverter mappingMongoConverter;

    @PostConstruct
    public void setMappingMongoConverter() {
         mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
