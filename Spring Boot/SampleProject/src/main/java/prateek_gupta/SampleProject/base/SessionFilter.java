package prateek_gupta.SampleProject.base;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SessionFilter implements Filter {
    String COOKIE_NAME = "pg";
    String COOKIE_SECRET =
            "Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25Pg25";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        Context.setCurrentContext(0);
        if (((HttpServletRequest) servletRequest).getCookies()!=null){
            ObjectNode cookieData;

            // Processing Cookies
            for (Cookie cookie : ((HttpServletRequest) servletRequest).getCookies())

                // Validating the cookie name
                if (cookie.getName().equals(COOKIE_NAME)) {

                    // Decoding JWT
                    cookieData = Util.getObjectMapper().valueToTree(Jwts.parser().verifyWith(
                                    Keys.hmacShaKeyFor(COOKIE_SECRET.getBytes()))
                            .build().parse(cookie.getValue()).getPayload());

                    // Setting context
                    Context.setCurrentContext(cookieData.get("userId").asInt());
                }
        }

        // Setting back cookie
        ((HttpServletResponse)servletResponse).addCookie(
                new Cookie(COOKIE_NAME,Jwts.builder().claim("userId",2)
                        .signWith(Keys.hmacShaKeyFor(COOKIE_SECRET.getBytes())).compact()));

        filterChain.doFilter(servletRequest, servletResponse);

        // Setting cookie here won't work as changes for the response headers will
        // be committed in the controller
    }
}
