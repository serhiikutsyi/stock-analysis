package com.serhiikutsyi.stock.processor.configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Serhii Kutsyi
 */
@Configuration
public class SparkConfig {

    @Value("${spark.application.name}")
    private String applicationName;

    @Value("${spark.master}")
    private String master;

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(applicationName)
                .setMaster(master);
    }

    @Bean
    public JavaSparkContext getJavaSparkContext(SparkConf sparkConf) {
        return new JavaSparkContext(sparkConf);
    }

    @Bean
    public JavaStreamingContext streamingContext(JavaSparkContext javaSparkContext) {
        return new JavaStreamingContext(javaSparkContext, Durations.seconds(2));
    }

}
