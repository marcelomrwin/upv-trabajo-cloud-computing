package es.upv.posgrado.api.repository;

import es.upv.posgrado.api.model.HotNews;
import es.upv.posgrado.api.model.HotNewsPagedResult;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NewsRepository implements PanacheRepository<HotNews> {
    public HotNewsPagedResult findAllPaged(String title, int index, int size){
        PanacheQuery<HotNews> all = findAll(Sort.by("id"));
        if (title!=null && !"".equals(title))
            all = find("lower(title) like concat('%',lower(?1),'%')",title);

        all.page(Page.of(index,size));

        HotNewsPagedResult result = HotNewsPagedResult.builder()
                .items(all.list())
                .totalPages(all.pageCount())
                .totalItems(all.count())
                .build();

        return result;
    }
}
