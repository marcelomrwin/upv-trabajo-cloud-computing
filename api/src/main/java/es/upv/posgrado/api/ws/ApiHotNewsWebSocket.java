package es.upv.posgrado.api.ws;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/hotnews/{clientId}")
@ApplicationScoped
@Slf4j
public class ApiHotNewsWebSocket {
    @Inject
    WsSessionManager wsSessionManager;

    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        log.debug("Receive new Session from client {}", clientId);
        wsSessionManager.addHotNewsSession(session, clientId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId) {
        log.debug("Closing Session for client {}", clientId);
        wsSessionManager.removeHotNewsSession(clientId);
    }

    @OnError
    public void onError(Session session, @PathParam("clientId") String clientId, Throwable throwable) {
        log.debug("Closing Session for client {} due error {}", clientId, throwable.getCause());
        wsSessionManager.removeHotNewsSession(clientId);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("clientId") String clientId) {
        if (message.equals("PONG")) log.debug("Receiving PONG from {}", clientId);
    }

}
