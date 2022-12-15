package es.upv.posgrado.executor.client.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidTagNameException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.TagOpt;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

//@ApplicationScoped
@RequestScoped
@Slf4j
public class GitClient {

    static final String REF_TAG_PREFIX = "refs/tags/";
    @Inject
    @ConfigProperty(name = "script.repo.url")
    String scriptRepoUrl;
    @Inject
    @ConfigProperty(name = "script.repo.tag")
    String scriptRepoTag;
    @Inject
    @ConfigProperty(name = "script.clonepath")
    String cloneDirectoryPath;

    public String cloneRepo() throws GitAPIException, IOException {
        File path = new File(cloneDirectoryPath);
        if (new File(path,".git").exists()) {
            //verify if the last tag is already cloned
            Repository repository = Git.open(path).getRepository();
            Ref ref = repository.findRef(scriptRepoTag);
            if (Objects.isNull(ref)) {
                log.info("Needs update cloned repo");
                path.delete();
            } else {
                log.info("The last version is already cloned");
                return cloneDirectoryPath;
            }
        }

        // Check if the requested tag exists in remote repository
        Map<String, Ref> refMap = Git.lsRemoteRepository().setRemote(scriptRepoUrl).setHeads(true).setTags(true).callAsMap();
        if (!refMap.containsKey(REF_TAG_PREFIX + scriptRepoTag))
            throw new InvalidTagNameException("Tag " + scriptRepoTag + " does not exists on remote repository");

        Git.cloneRepository().setURI(scriptRepoUrl).setDirectory(path).setTagOption(TagOpt.FETCH_TAGS).call().close();

        return cloneDirectoryPath;

    }
}
