package es.upv.posgrado.client.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class CommandExecutor {

    public static String executeCommand(String... commands) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);
        Process process = builder.start();
        StringBuilder sb = new StringBuilder();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), c->{sb.append(c);});
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        boolean exitCode = process.waitFor(5L, TimeUnit.SECONDS);
        if (!exitCode) throw new AssertionError("Fail in script execution");
        future.get(5, TimeUnit.SECONDS);

        return sb.toString();
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}
