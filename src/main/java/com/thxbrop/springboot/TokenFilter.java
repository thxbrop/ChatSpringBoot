package com.thxbrop.springboot;

import com.thxbrop.springboot.entity.Token;
import com.thxbrop.springboot.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter("/*")
@Component
public class TokenFilter implements Filter {
    private final TokenService tokenService;

    @Autowired
    public TokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/user/login") || requestURI.contains("/user/register")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        if (requestURI.contains("/user") || requestURI.contains("/con") || requestURI.contains("/member") || requestURI.contains("/invite") || requestURI.contains("/message")) {
            Optional.ofNullable(request.getHeader("userId")).ifPresentOrElse(h -> {
                int userId = Integer.parseInt(h);
                String token = request.getHeader("token");
                Token peek = tokenService.peek(userId);
                if (peek != null && peek.getToken().equals(token)) {
                    try {
                        filterChain.doFilter(servletRequest, servletResponse);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, () -> {
                try {
                    response.sendError(403, "Token Verification Failed");
                } catch (IOException ignored) {
                }
            });
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
