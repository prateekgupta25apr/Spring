package prateek_gupta.SampleProject.multitenancy;

import lombok.Getter;
import prateek_gupta.SampleProject.prateek_gupta.Init;

/**
 * Class to store current tenant's details
 */
@Getter
public class TenantContext {
    /**
     * Field to store the Tenant details in a Thread safe way
     */
    private static final ThreadLocal<TenantContext> currentTenant =
            new InheritableThreadLocal<>();

    String schemaName;

    String baseUrl;
    String apiUrl;

    public TenantContext(String schemaName) {
        this.schemaName = schemaName;
    }

    public static TenantContext getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(new TenantContext(tenant));
    }

    public static void clear() {
        currentTenant.remove();
    }

    public static String getDefaultSchemaName() {
        return Init.getConfiguration("db_default_schema").toString();
    }
}
