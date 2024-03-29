package ru.itis.platform.security.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.itis.platform.security.auth.JwtAuthentication;
import ru.itis.platform.security.providers.JwtAuthenticationProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {
    private final static String AUTH_HEADER = "Authorization";
    private final JwtAuthenticationProvider provider;
    private final JwtAuthentication authentication;

    @Autowired
    public JwtAuthenticationFilter(JwtAuthenticationProvider provider, JwtAuthentication authentication) {
        this.provider = provider;
        this.authentication = authentication;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String headerValue = request.getHeader(AUTH_HEADER);
        if (headerValue != null) {
            authentication.setToken(headerValue);
            SecurityContextHolder.getContext().setAuthentication(provider.authenticate(authentication));
        } else {
            authentication.setAuthenticated(false);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}