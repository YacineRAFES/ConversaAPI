package fr.afpa.dev.pompey.conversaapi.securite;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class JWTFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
            return;
        }

        if (!"Bearer MON_TOKEN_SECRET".equals(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Invalid");

        }
    }
}
