package es.upv.posgrado.connectors.mediastack.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    public int limit;
    public int offset;
    public int count;
    public int total;
}
