package cristina.tech.fancydress.store.domain;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.Serializable;

/**
 * Using UUID given in the Producer payload as payload key for Entity database ID, when possible, otherwise rely
 * on identity generation.
 */
@Slf4j
public class UseUUIDOrGenerate extends IdentityGenerator {

    private static final String TRANSIENT_PRODUCER_UUID_PROPERTY_NAME = "uuid";

    @Override
    public Serializable generate(SessionImplementor session, Object object) {

        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);

        if (id != null) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Found entity id %s", id));
            }
            return id;
        }

        String uuid = null;
        try {
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
            uuid = (String) wrapper.getPropertyValue(TRANSIENT_PRODUCER_UUID_PROPERTY_NAME);
        } catch (Exception ignore) {
            // ignore
        }

        if (uuid != null) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("No entity id, found and using producer uuid %s as entity id", uuid));
            }
            return uuid;
        }

        log.debug("No entity id or producer uuid found, generating one");

        return super.generate(session, object);
    }

}


