package faang.school.postservice.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.postservice.redisPublisher.EventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class LikeEventPublisher extends EventPublisher {
//    private final static ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private ChannelTopic likeEventChannel;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Override
//    public void publish(LikeEvent likeEvent) {
//        String jsonProfileViewEvent;
//        try {
//            jsonProfileViewEvent = objectMapper.writeValueAsString(likeEvent);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        redisTemplate.convertAndSend(likeEventChannel.getTopic(), jsonProfileViewEvent);
//    }
    public LikeEventPublisher(RedisTemplate<String, Object> redisTemplate,
                             ObjectMapper objectMapper,
                             @Qualifier("likeEventTopic") ChannelTopic channelTopic) {
        super(redisTemplate, objectMapper, channelTopic);
    }
}
