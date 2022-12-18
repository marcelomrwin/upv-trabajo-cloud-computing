package es.upv.posgrado.transaction;

public enum SagaStatus {
    CREATED, SENT, PROCESSING, PROCESSED, ROLLBACK, TO_COMPENSATE
}
