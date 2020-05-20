package cn.xdean.spring.ex.websocket.topic;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
public class WebSocketTopicProperties {
    String path = "/socket/topic";
}
