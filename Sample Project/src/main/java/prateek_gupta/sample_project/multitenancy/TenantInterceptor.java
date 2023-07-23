package prateek_gupta.sample_project.multitenancy;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to intercept the tenant id and set the {@link TenantContext}
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String tenantIdStr=request.getHeader("tenantId");
        if (tenantIdStr != null) {
            try {
                int tenantId = Integer.parseInt(tenantIdStr);
                TenantContext.setCurrentTenant("sample_project_" + tenantId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        TenantContext.clear();
    }
}
