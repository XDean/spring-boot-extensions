package cn.xdean.spring.ex.websocket.topic;

public interface WebSocketPayloadHolder {
    <T> T getPayload(Class<T> clz);
}
