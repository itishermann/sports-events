package com.etna.sportevents.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String jwtHeaderName;

    @Value("${jwt.prefix}")
    private String jwtPrefix;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader(jwtHeaderName);

		Integer userId = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith(jwtPrefix)) {
			jwtToken = jwtTokenUtil.extractTokenFromHeader(requestTokenHeader);
			try {
				userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		}

		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			JwtUserDetails jwtUserDetails = this.jwtUserDetailsService.loadUserById(userId);

			if (jwtTokenUtil.validateToken(jwtToken, jwtUserDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						jwtUserDetails, null, jwtUserDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}
