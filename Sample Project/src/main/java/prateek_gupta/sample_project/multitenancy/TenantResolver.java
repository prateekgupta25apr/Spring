package prateek_gupta.sample_project.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import static prateek_gupta.sample_project.multitenancy.TenantContext.DEFAULT_SCHEMA_NAME;

@Component
public class TenantResolver implements CurrentTenantIdentifierResolver {

    /**
     * This method will return the name of the schema to connect to
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        String schemaName = TenantContext.getCurrentTenant()!=null?
                TenantContext.getCurrentTenant().getSchemaName():null;
        if (schemaName != null) {
            return schemaName;
        }
        return DEFAULT_SCHEMA_NAME;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
