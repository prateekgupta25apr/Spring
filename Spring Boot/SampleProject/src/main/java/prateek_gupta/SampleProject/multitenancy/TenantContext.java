package prateek_gupta.SampleProject.multitenancy;

import lombok.Getter;

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

    public static final String DEFAULT_SCHEMA_NAME = "sample_project_1";

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
}
