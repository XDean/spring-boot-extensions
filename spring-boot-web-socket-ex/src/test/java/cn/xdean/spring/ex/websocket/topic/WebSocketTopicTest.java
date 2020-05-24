package cn.xdean.spring.ex.websocket.topic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableAutoConfiguration
@ContextConfiguration(classes = WebSocketTopicTest.Topic.class)
public class WebSocketTopicTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    WebSocketTopicHandler handler;

    @MockBean
    WebSocketSession session;

    @Test
    void testNoTopic() {
        assertThrows(IllegalArgumentException.class, () ->
                handler.handleTextMessage(session, new TextMessage("{}")));
    }

    @Component
    public static class Topic implements WebSocketTopic {
        @Override
        public String topic() {
            return "test";
        }

        @Override
        public WebSocketTopicEventHandler create(WebSocketSession session) {
            return (s, event, holder) -> {
                if (!event.equals("test")) {
                    throw new IllegalArgumentException("must be test");
                }
            };
        }
    }
}
