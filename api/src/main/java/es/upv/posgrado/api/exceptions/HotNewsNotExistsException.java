package es.upv.posgrado.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HotNewsNotExistsException extends RuntimeException {
    private Long hotNewsId;
}
