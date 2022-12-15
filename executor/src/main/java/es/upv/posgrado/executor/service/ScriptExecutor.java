package es.upv.posgrado.executor.service;

import es.upv.posgrado.executor.client.git.GitClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

@ApplicationScoped
public class ScriptExecutor {

    @Inject
    GitClient gitClient;

    @Inject
    @ConfigProperty(name = "script.name")
    String scriptName;


    public String executeScriptCommand(String jsonParameterPath) throws Exception {

        String scriptPath = gitClient.cloneRepo()+ File.separator+scriptName;
        ProcessBuilder builder = new ProcessBuilder();
        String[] commands = new String[]{"python3",scriptPath,jsonParameterPath};
        builder.command(commands);
        Process process = builder.start();
        StringBuilder sb = new StringBuilder();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), c->{sb.append(c);});
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        boolean exitCode = process.waitFor(5L,TimeUnit.SECONDS);
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
