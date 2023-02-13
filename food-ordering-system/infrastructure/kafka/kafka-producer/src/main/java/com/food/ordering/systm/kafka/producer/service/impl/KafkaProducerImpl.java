package com.food.ordering.systm.kafka.producer.service.impl;

import com.food.ordering.systm.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.systm.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.io.Serializable;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K,V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topic, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("Sending message={} to topic={}",message,topic);
        try {
            ListenableFuture<SendResult<K,V>> kafkaResultFuture =  kafkaTemplate.send(topic,key,message);
            kafkaResultFuture.addCallback(callback);
        } catch (KafkaException exception) {
            log.error("Error on Kafka producer with key: {} with message: {} and exception: {}",key,message,exception.getMessage());
            throw new KafkaProducerException("Error on Kafka producer with key:"+key+" and message:"+message);
        }
    }

    @PreDestroy
    public void close(){
        if(kafkaTemplate != null){
            log.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }
}