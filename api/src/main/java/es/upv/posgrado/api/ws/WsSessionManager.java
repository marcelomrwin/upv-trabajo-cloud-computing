package es.upv.posgrado.api.ws;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.vertx.ConsumeEvent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@Slf4j
public class WsSessionManager {
    @Inject
    ManagedExecutor executor;
    private Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(Session session, String clientId) {
        sessions.put(clientId, session);
    }

    public void sentResponse(String message, String clientId) {
        sessions.get(clientId).getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                log.error("Unable to send message", result.getException());
            }
        });
    }

    public CompletionStage<Void> broadcast(Object message) {
        return CompletableFuture.runAsync(() -> {
            sessions.values().forEach(s -> {
                s.getAsyncRemote().sendObject(message, result -> {
                    if (result.getException() != null) {
                        log.error("Unable to send message", result.getException());
                    }
                });
            });
        }, executor);
    }

    public void removeSession(String clientId) {
        sessions.remove(clientId);
    }

    @ConsumeEvent("hotnews")
    public void publishWsMessage(String message) {
        broadcast(message);
    }

    @Scheduled(every = "10s")
    void ping() {
        sessions.keySet().forEach(k -> {
            sessions.get(k).getAsyncRemote().sendText("PING",rs -> {
                if (rs.getException()!=null){
                    log.error("Fail sending PING to session "+k);
                    removeSession(k);
                }
            });
        });
    }
}
