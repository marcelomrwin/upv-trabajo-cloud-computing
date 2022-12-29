package es.upv.posgrado.api.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upv.posgrado.common.model.Job;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.eventbus.Message;
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
    @Inject
    ObjectMapper objectMapper;
    private Map<String, Session> hotNewsSessions = new ConcurrentHashMap<>();
    private Map<String, Session> jobSessions = new ConcurrentHashMap<>();

    private Map<String, Session> jobStatisticsSessions = new ConcurrentHashMap<>();

    public void addHotNewsSession(Session session, String clientId) {
        hotNewsSessions.put(clientId, session);
    }

    public void addJobSession(Session session, String clientId) {
        jobSessions.put(clientId, session);
    }

    public void addJobStatisticsSession(Session session, String clientId) {
        jobStatisticsSessions.put(clientId, session);
    }

    public void sentHotNewsResponse(String message, String clientId) {
        hotNewsSessions.get(clientId).getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                log.error("Unable to send hotnews message", result.getException());
            }
        });
    }

    public CompletionStage<Void> notifyJobResponse(String message, String clientId) {
        return CompletableFuture.runAsync(() -> {
            jobSessions.get(clientId).getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    log.error("Unable to send job message", result.getException());
                }
            });
        }, executor);
    }

    public CompletionStage<Void> broadcastHotNews(Object message) {
        return CompletableFuture.runAsync(() -> {
            hotNewsSessions.values().forEach(s -> {
                s.getAsyncRemote().sendObject(message, result -> {
                    if (result.getException() != null) {
                        log.error("Unable to send message", result.getException());
                    }
                });
            });
        }, executor);
    }

    public void removeHotNewsSession(String clientId) {
        hotNewsSessions.remove(clientId);
    }

    public void removeJobSession(String clientId) {
        jobSessions.remove(clientId);
    }

    public void removeJobStatisticsSession(String clientId) {
        jobStatisticsSessions.remove(clientId);
    }

    @ConsumeEvent("hotnews")
    public void publishHotNewsWsMessage(String message) {
        broadcastHotNews(message);
    }

    @ConsumeEvent("job")
    public void publishJobMessage(Message<String> msg) {
        try {
            Job job = objectMapper.readValue(msg.body(), Job.class);
            notifyJobResponse(msg.body(), job.getSubmittedBy());
        } catch (JsonProcessingException e) {
            log.error("Fail sending response to WS",e);
        }
    }

    @ConsumeEvent("job-stats")
    public void publishJobStats(String message){
        broadcastJobStatistics(message);
    }

    @Scheduled(every = "30s")
    void ping() {
        hotNewsSessions.keySet().forEach(k -> {
            Session session = hotNewsSessions.get(k);
            if (session.isOpen())
                session.getAsyncRemote().sendText("PING", rs -> {
                    if (rs.getException() != null) {
                        log.error("Fail sending PING to session " + k);
                        removeHotNewsSession(k);
                    }
                });
            else
                removeHotNewsSession(k);
        });

        jobSessions.keySet().forEach(k -> {
            Session session = jobSessions.get(k);
            if (session.isOpen())
                session.getAsyncRemote().sendText("PING", rs -> {
                    if (rs.getException() != null) {
                        log.error("Fail sending PING to session " + k);
                        removeJobSession(k);
                    }
                });
            else
                removeJobSession(k);
        });
    }

    public CompletionStage<Void> broadcastJobStatistics(Object message) {
        return CompletableFuture.runAsync(() -> {
            jobStatisticsSessions.values().forEach(s -> {
                s.getAsyncRemote().sendObject(message, result -> {
                    if (result.getException() != null) {
                        log.error("Unable to send message", result.getException());
                    }
                });
            });
        }, executor);
    }
}
