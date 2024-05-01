package com.thegame.websocket.kafka;

import com.thegame.websocket.eureka.EurekaClientInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    private final EurekaClientInfo eurekaClientInfo;
    private final SimpMessagingTemplate messagingTemplate;


    public KafkaListenerService(EurekaClientInfo eurekaClientInfo, SimpMessagingTemplate messagingTemplate) {
        this.eurekaClientInfo = eurekaClientInfo;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(groupId = "#{eurekaClientInfo.getInstanceId()}", topics = "#{eurekaClientInfo.getInstanceId()}")
    public void listen(ConsumerRecord<String, KafkaEvent> kafkaEvent) {
        KafkaEvent event = kafkaEvent.value();
        messagingTemplate.convertAndSendToUser(event.receiverId(), event.destination(), event.notification());
        }
    }

