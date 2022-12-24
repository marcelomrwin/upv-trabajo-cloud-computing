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
        log.info("Receive new Session from client {}", clientId);
        wsSessionManager.addSession(session, clientId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId) {
        log.info("Closing Session for client {}", clientId);
        wsSessionManager.removeSession(clientId);
    }

    @OnError
    public void onError(Session session, @PathParam("clientId") String clientId, Throwable throwable) {
        log.info("Closing Session for client {} due error {}", clientId, throwable.getCause());
        wsSessionManager.removeSession(clientId);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("clientId") String clientId) {
        if (message.equals("PONG")) log.info("Receiving PONG from {}", clientId);
    }

}
