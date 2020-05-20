package cn.xdean.spring.ex.websocket.topic;

import java.io.IOException;

public interface WebSocketPayloadHolder {
    <T> T getPayload(Class<T> clz) throws IOException;
}
