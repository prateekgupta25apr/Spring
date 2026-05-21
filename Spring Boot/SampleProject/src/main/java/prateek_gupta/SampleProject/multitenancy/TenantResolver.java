package prateek_gupta.SampleProject.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantResolver implements CurrentTenantIdentifierResolver<String> {

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
        return TenantContext.getDefaultSchemaName();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
