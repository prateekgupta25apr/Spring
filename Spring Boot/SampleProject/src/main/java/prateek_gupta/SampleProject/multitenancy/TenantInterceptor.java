package prateek_gupta.SampleProject.multitenancy;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import prateek_gupta.SampleProject.prateek_gupta.Init;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Class to intercept the tenant id and set the {@link TenantContext}.
 * This interceptor is registered via
 * {@link TenantInterceptor_And_DBConnectionProvider_Registration} class.
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    /**
     * This method will be called everytime before processing an API
     */
    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
            @Nonnull Object handler) {
        String dbSchemaPrefix= Init.getConfiguration("db_schema_prefix").toString();

        if (StringUtils.isNotBlank(dbSchemaPrefix)){
            String tenantIdStr=request.getHeader("tenant_id");
            if (StringUtils.isNotBlank(tenantIdStr)) {
                try {
                    int tenantId = Integer.parseInt(tenantIdStr);
                    TenantContext.setCurrentTenant("sample_project_" + tenantId);

                    TenantContext.getCurrentTenant().baseUrl =
                            Init.getConfiguration("base_url").toString();
                    TenantContext.getCurrentTenant().apiUrl =
                            Init.getConfiguration("api_url").toString();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            else
                TenantContext.setCurrentTenant(TenantContext.getDefaultSchemaName());
        }
        return true;
    }

    /**
     * This method will be called everytime after an API is processed
     */
    @Override
    public void postHandle(
            @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
            @Nonnull Object handler, ModelAndView modelAndView) {
        TenantContext.clear();
    }
}
