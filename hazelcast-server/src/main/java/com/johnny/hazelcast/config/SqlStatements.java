package com.johnny.hazelcast.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

/**
 * Allows SQL statements to be loaded from external configuration.
 * 
 * ConfigurationProperties doesn't allow the configuration to be stored anywhere
 * than application.properties, which is a shame, so there's a lot of @Value
 * annotations - at least Lombok keeps it small :)
 * 
 * @author johnny
 *
 */
@Getter
@Configuration
@PropertySource("classpath:sql-statements-${db.target:mysql}.properties")
public class SqlStatements {

    @Value("${map.insert}")
    private String mapInsert;

    @Value("${map.select.single}")
    private String mapSelectSingle;

    @Value("${map.select.all}")
    private String mapSelectAll;

    @Value("${map.delete}")
    private String mapDelete;

    @Value("${queue.insert}")
    private String queueInsert;

    @Value("${queue.delete}")
    private String queueDelete;

    @Value("${queue.delete.keys}")
    private String queueDeleteKeys;

    @Value("${queue.select}")
    private String queueSelect;

    @Value("${queue.select.keys}")
    private String queueSelectKeys;

    @Value("${queue.select.all}")
    private String queueSelectAll;

    @Value("${queue.key}")
    private String queueKey;

    @Value("${queue.item}")
    private String queueItem;

    @Value("${queue.table}")
    private String queueTable;

    @Value("${queue.name}")
    private String queueName;

}
