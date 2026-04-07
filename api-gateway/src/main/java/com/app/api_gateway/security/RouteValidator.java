package com.app.api_gateway.security;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {
	
	public static final List<String> openApiEndpoints = List.of(
			"/auth/signup",
			"/auth/signin",
			"/auth/login",
			"/eureka");
	
	public Predicate<ServerHttpRequest> isSecured =request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

	  public Map<String, List<String>> roleAccessMap = Map.of(
	            "/admin", List.of("ADMIN"),
	            "/user", List.of("USER", "ADMIN")
	    );
}
