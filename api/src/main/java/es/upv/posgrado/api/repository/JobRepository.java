package es.upv.posgrado.api.repository;

import es.upv.posgrado.api.model.Job;
import es.upv.posgrado.api.model.JobPagedResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JobRepository implements PanacheRepository<Job> {

    public JobPagedResult findAllPaged(Long id, String title, int index, int size) {
        PanacheQuery<Job> all = findAll(Sort.descending("requestedAt", "processedAt"));
        if (id != null && title != null)
            all = find("id = ?1 and lower(title) like concat('%',lower(?2),'%')", id, title);
        else if (id != null)
            all = find("id = ?1", id);
        else if (title != null && !"".equals(title))
            all = find("lower(title) like concat('%',lower(?1),'%')", title);

        all.page(Page.of(index, size));

        JobPagedResult result = JobPagedResult.builder()
                .items(all.list())
                .totalPages(all.pageCount())
                .totalItems(all.count())
                .build();

        return result;
    }

    public JobPagedResult findSubmittedBy(String userName, Long id, String title, int index, int size) {
        PanacheQuery<Job> jobSubmitted = find("#Job.findAllSubmittedBy", userName);

        if (id != null && title != null) {
            jobSubmitted = find("#Job.findByIdAndTitleSubmittedBy", id, userName, title);
        } else if (id != null) {
            jobSubmitted = find("#Job.findByIdSubmittedBy", id, userName);
        } else if (title != null && !"".equals(title)) {
            jobSubmitted = find("#Job.findByTitleSubmittedBy", userName, title);
        }

        jobSubmitted.page(Page.of(index, size));

        JobPagedResult result = JobPagedResult.builder()
                .items(jobSubmitted.list())
                .totalPages(jobSubmitted.pageCount())
                .totalItems(jobSubmitted.count())
                .build();

        return result;
    }

}