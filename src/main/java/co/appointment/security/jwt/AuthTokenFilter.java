package co.appointment.security.jwt;

import co.appointment.security.service.UserDetailsServiceImpl;
import co.appointment.shared.constant.TokenConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private static final String BEARER = "Bearer ";


    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = parseJwtToken(request);
            if(StringUtils.hasText(jwtToken) && jwtUtils.validateJwtToken(jwtToken)) {
                String username = jwtUtils.extractClaimByKey(jwtToken, TokenConstants.EMAIL);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
        filterChain.doFilter(request, response);
    }
    private String parseJwtToken(final HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(BEARER.length());
        }
        return null;
    }
}
