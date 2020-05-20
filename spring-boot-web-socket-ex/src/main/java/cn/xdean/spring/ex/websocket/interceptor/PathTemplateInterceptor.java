package cn.xdean.spring.ex.websocket.interceptor;

import cn.xdean.spring.ex.websocket.WebSocketProvider;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

public class PathTemplateInterceptor implements HandshakeInterceptor {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final ConversionService conversionService;

    public PathTemplateInterceptor(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (wsHandler instanceof WebSocketHandlerDecorator) {
            wsHandler = ((WebSocketHandlerDecorator) wsHandler).getLastHandler();
        }
        if (!(wsHandler instanceof WebSocketProvider)) {
            return true;
        }
        Map<String, String> vars = pathMatcher.extractUriTemplateVariables(((WebSocketProvider) wsHandler).path(), request.getURI().getPath());
        attributes.put(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, vars);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // do nothing
    }

    public Optional<String> getVariable(WebSocketSession session, String key) {
        return getVariable(session, key, String.class);
    }

    public <T> Optional<T> getVariable(WebSocketSession session, String key, Class<T> targetClass) throws ConversionException {
        Object vars = session.getAttributes().get(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (!(vars instanceof Map)) {
            return Optional.empty();
        }
        Object value = ((Map) vars).get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(conversionService.convert(value, targetClass));
    }
}
