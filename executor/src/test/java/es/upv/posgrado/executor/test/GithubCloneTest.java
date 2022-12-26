package es.upv.posgrado.executor.test;

import es.upv.posgrado.executor.client.git.GitClient;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

@QuarkusTest
public class GithubCloneTest {

    @Inject
    GitClient client;
    @Test
    public void cloneRepoTest() throws GitAPIException, IOException {
        String path = client.cloneRepo();
        Assertions.assertNotNull(path);
        System.out.println(path);
    }
}
