package es.upv.posgrado.executor.service;

import es.upv.posgrado.executor.client.git.GitClient;
import es.upv.posgrado.client.shell.CommandExecutor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;

@ApplicationScoped
public class ScriptExecutor {

    @Inject
    GitClient gitClient;

    @Inject
    @ConfigProperty(name = "script.name")
    String scriptName;



    public String executeScriptCommand(String jsonParameterPath) throws Exception {

        String scriptPath = gitClient.cloneRepo()+ File.separator+scriptName;

        return CommandExecutor.executeCommand(new String[]{"python3",scriptPath,jsonParameterPath});

    }

}
