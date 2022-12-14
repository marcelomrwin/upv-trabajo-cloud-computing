package es.upv.posgrado.api.repository;

import es.upv.posgrado.api.model.Job;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JobRepository implements PanacheRepository<Job> {

}
