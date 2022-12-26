package es.upv.posgrado.executor.service;

import es.upv.posgrado.client.shell.CommandExecutor;
import es.upv.posgrado.executor.client.git.GitClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.File;

@ApplicationScoped
public class ScriptExecutor {

    @Inject
    GitClient gitClient;

    @Inject
    @ConfigProperty(name = "script.name")
    String scriptName;

    @Inject
    Instance<CommandExecutor> commandExecutor;

    public String executeScriptCommand(String jsonParameterPath) throws Exception {

        String scriptPath = gitClient.cloneRepo() + File.separator + scriptName;

        return commandExecutor.get().executeCommand(new String[]{"python3", scriptPath, jsonParameterPath});

    }

}
