package com.docflow.inbox_service.filter;

import com.docflow.inbox_service.config.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;

        // try to fetch token from header
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
            jwt = jwtHeader.substring(7); // remove "Bearer" prefix
        }

        // if not in header, try to fetch token from cookies
        if (jwt == null && request.getCookies() != null) {
            for (var cookie: request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // apply logic
        if (jwt != null) {
            // fetch secret key and prepare it
            Environment env = getEnvironment();
            String secretValue = env.getProperty("JWT_SECRET");
            if (secretValue == null) {
                throw new IllegalStateException("Internal Server Error: Unable to authentication");
            }
            SecretKey secretKey = Keys.hmacShaKeyFor(secretValue.getBytes(StandardCharsets.UTF_8));

            try {
                // build jwt parser, parse and validate the token
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                // extract useful information from payloads
                String id = String.valueOf(claims.get("id"));
                String username = String.valueOf(claims.get("username"));
                String email = String.valueOf(claims.get("email"));
                String role = String.valueOf(claims.get("role"));

                // save this information in context holder to use it later
                // and to inform spring security that the user already authenticated
                UserPrincipal userPrincipal = new UserPrincipal(id, username, email, role);
                Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

                // invoke next filter
                filterChain.doFilter(request, response);

            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                response.getWriter().flush();
                response.getWriter().close();
            }

        } else { // if not in header or cookies
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: No token provided\"}");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/actuator/health") ||
                request.getServletPath().startsWith("/swagger-ui") ||
                request.getServletPath().startsWith("/v3/api-docs");
    }
}