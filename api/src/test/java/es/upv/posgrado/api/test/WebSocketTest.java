package es.upv.posgrado.api.test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.websocket.*;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@QuarkusTest
@Slf4j
public class WebSocketTest {
    @TestHTTPResource("/ws/hotnews/test")
    URI uri;

    @Test
    public void testConnection()throws Exception{
        try(Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)){
            session.getAsyncRemote().sendObject("My Message");
            TimeUnit.SECONDS.sleep(5);
        }
    }

    @ClientEndpoint
    public static class Client {
        @OnOpen
        public void open(Session session) {
            log.info("OPEN SESSION " + session.getId());
        }
        @OnMessage
        public void message(String msg){
            log.info("NEW MESSAGE\n{}",msg);
        }
    }
}
