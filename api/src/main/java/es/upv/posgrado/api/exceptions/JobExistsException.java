package es.upv.posgrado.api.exceptions;

import es.upv.posgrado.common.model.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JobExistsException extends RuntimeException {
    private Long jobId;
    private JobStatus jobStatus;
}
