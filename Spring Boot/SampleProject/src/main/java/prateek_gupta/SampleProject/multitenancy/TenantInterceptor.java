package prateek_gupta.SampleProject.multitenancy;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to intercept the tenant id and set the {@link TenantContext}
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Value("${db_schema_prefix}")
    String dbSchemaPrefix;

    /**
     * This method will be called everytime before processing an API
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        if (StringUtils.isNotBlank(dbSchemaPrefix)){
            String tenantIdStr=request.getHeader("tenant_id");
            if (StringUtils.isNotBlank(tenantIdStr)) {
                try {
                    int tenantId = Integer.parseInt(tenantIdStr);
                    TenantContext.setCurrentTenant("sample_project_" + tenantId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return true;
    }

    /**
     * This method will be called everytime after an API is processed
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        TenantContext.clear();
    }
}
