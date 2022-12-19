package es.upv.posgrado.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class HotNewsPagedResult extends PagedResult<HotNews> {

}
