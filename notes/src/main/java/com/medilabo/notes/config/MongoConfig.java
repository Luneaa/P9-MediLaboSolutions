package com.medilabo.notes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory, MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDatabaseFactory), context);
        // Supprime le champ _class automatiquement ajouté par Spring Data MongoDB pour simplifier les données stockées,
        // car nous n'utilisons pas l'héritage dans nos documents MongoDB.
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        
        // Activer les conversions JSR-310 (Java 8 Date/Time API) par défaut
        converter.setCustomConversions(mongoCustomConversions());
        converter.afterPropertiesSet();
        
        return new MongoTemplate(mongoDatabaseFactory, converter);
    }
    
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(java.util.Collections.emptyList());
    }
}
