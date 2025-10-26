package co.appointment.service;

import co.appointment.config.AppConfigProperties;
import co.appointment.kafka.event.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<Map<String, Object>, EmailEvent> kafkaTemplate;
    private final AppConfigProperties appConfigProperties;;

    public  void publishEvent(final EmailEvent event,
                              final Map<String, Object> eventKey,
                              final Map<String, Object> eventHeaders) {
        kafkaTemplate.send(new ProducerRecord<>(
                appConfigProperties.getKafka().getNotificationTopic(),
                null,
                System.currentTimeMillis(),
                eventKey,
                event,
                getEventHeaders(eventHeaders)));
    }
    private List<Header> getEventHeaders(final Map<String, Object> eventHeaders) {
        List<Header> headers = new ArrayList<>();
        if(eventHeaders.isEmpty()) {
            return headers;
        }
        eventHeaders.forEach((key, value) -> headers.add(new RecordHeader(key, value.toString().getBytes(StandardCharsets.UTF_8))));

        return headers;
    }
}
