package com.expleo.users.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expleo.users.exception.ApiException;
import com.expleo.users.exception.ApiExceptionBody;
import com.expleo.users.model.UserSchema;
import com.expleo.users.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String id = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				id = jwtTokenUtil.getUserIdFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserSchema userDetails = this.userService.findById(id);
			if (userDetails!=null && jwtTokenUtil.validateToken(jwtToken)) {
				try {
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				
				if(userDetails.getUsername().equals("admin"))
				{
					authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, authorities);
					
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}else {
					 ApiExceptionBody body=new ApiExceptionBody();
					 body.setType("Error");
					 body.setMessage("Invalid User");
					 //response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized user");
					 throw new ApiException(HttpStatus.FORBIDDEN, body );
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			}
		}
		chain.doFilter(request, response);
	}

}