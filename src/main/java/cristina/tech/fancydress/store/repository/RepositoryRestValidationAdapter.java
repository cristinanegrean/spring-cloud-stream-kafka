package cristina.tech.fancydress.store.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


/**
 * There are two ways to register a Validator instance in Spring Data REST: wire it by bean name or register the validator manually.
 * We are going to register HibernateValidator manually. For all JPA Entities, validations rules are defined inline via annotations.
 */
@Configuration
public class RepositoryRestValidationAdapter extends RepositoryRestConfigurerAdapter {

    /**
     * The basic configuration above will trigger Bean Validation to initialize using its default bootstrap mechanism. A JSR-303/JSR-349 provider, such as Hibernate Validator,
     * is expected to be present in the classpath and will be detected automatically.
     */
    @Bean
    @Primary
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * Overridding default behavior by adding HibernateValidator instance manually
     * for turning on Bean Validation feature on.
     *
     * @param validatingListener The {@link org.springframework.context.ApplicationListener}
     *                           responsible for invoking the HibernateValidator
     *                           instance after the correct event.
     */
    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        Validator validator = validator();
        validatingListener.addValidator("beforeCreate", validator); // POST
        validatingListener.addValidator("beforeSave", validator); // PUT + PATCH
    }
}

