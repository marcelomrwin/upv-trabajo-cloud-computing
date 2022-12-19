package es.upv.posgrado.api.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@SuperBuilder
public abstract class PagedResult<T> {

    protected long totalItems;
    protected List<T> items;
    protected int totalPages;
    protected int currentPage;
}
