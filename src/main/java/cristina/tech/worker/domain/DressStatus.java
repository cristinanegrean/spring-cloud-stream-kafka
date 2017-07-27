package cristina.tech.worker.domain;

/**
 * The {@link DressStatus} describes the state of an {@link Dress}.
 * The aggregate state of a {@link Dress} is sourced from attached domain
 * events in the form of {@link cristina.tech.worker.event.DressEvent}.
 */
public enum DressStatus {
    CREATED, UPDATED, RATED
}
