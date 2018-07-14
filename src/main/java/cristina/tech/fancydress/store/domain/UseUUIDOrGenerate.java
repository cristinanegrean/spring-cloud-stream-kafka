package cristina.tech.fancydress.store.domain;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.Serializable;

/**
 * Using UUID given in the Producer payload as payload key for Entity database ID, when possible, otherwise rely
 * on identity generation.
 */
public class UseUUIDOrGenerate extends IdentityGenerator {

    private static final String TRANSIENT_PRODUCER_UUID_PROPERTY_NAME = "uuid";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {

        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);

        if (id != null) {
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
            return uuid;
        }

        return super.generate(session, object);
    }

}
