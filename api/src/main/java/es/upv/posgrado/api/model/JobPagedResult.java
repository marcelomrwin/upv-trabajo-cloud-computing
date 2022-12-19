package es.upv.posgrado.api.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JobPagedResult extends PagedResult<Job> {
}
